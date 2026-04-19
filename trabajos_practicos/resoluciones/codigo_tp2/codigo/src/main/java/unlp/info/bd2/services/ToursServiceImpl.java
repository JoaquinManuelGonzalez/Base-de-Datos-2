package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import unlp.info.bd2.dto.RoutePurchaseSummaryDTO;
import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.ItemServiceRepository;
import unlp.info.bd2.repositories.PurchaseRepository;
import unlp.info.bd2.repositories.ReviewRepository;
import unlp.info.bd2.repositories.RouteRepository;
import unlp.info.bd2.repositories.ServiceRepository;
import unlp.info.bd2.repositories.StopRepository;
import unlp.info.bd2.repositories.SupplierRepository;
import unlp.info.bd2.repositories.UserRepository;
import unlp.info.bd2.utils.ToursException;

@Service
@Transactional
public class ToursServiceImpl implements ToursService {

    private final UserRepository userRepository;

    private final RouteRepository routeRepository;

    private final PurchaseRepository purchaseRepository;

    private final ServiceRepository serviceRepository;

    private final SupplierRepository supplierRepository;

    private final ReviewRepository reviewRepository;

    private final StopRepository stopRepository;

    private final ItemServiceRepository itemServiceRepository;

    private final EntityManager entityManager;

    public ToursServiceImpl(PurchaseRepository purchaseRepository,
            ReviewRepository reviewRepository,
            RouteRepository routeRepository,
            ServiceRepository serviceRepository,
            SupplierRepository supplierRepository,
            UserRepository userRepository,
            StopRepository stopRepository,
            ItemServiceRepository itemServiceRepository,
            EntityManager entityManager) {
        this.purchaseRepository = purchaseRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.serviceRepository = serviceRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.stopRepository = stopRepository;
        this.itemServiceRepository = itemServiceRepository;
        this.entityManager = entityManager;
    }

    // ─── USER ──────────────────────────────────────────

    @Override
    public User createUser(String username, String password, String fullName,
            String email, Date birthdate, String phoneNumber) throws ToursException {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(fullName);
            user.setEmail(email);
            user.setBirthdate(birthdate);
            user.setPhoneNumber(phoneNumber);
            user.setActive(true);
            User saved = userRepository.save(user);
            // Forzamos flush para que el constraint unique se evalúe dentro de esta transacción
            entityManager.flush();
            return saved;
        } catch (Exception e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    public DriverUser createDriverUser(String username, String password,
            String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        try {
            DriverUser driver = new DriverUser();
            driver.setUsername(username);
            driver.setPassword(password);
            driver.setName(fullName);
            driver.setEmail(email);
            driver.setBirthdate(birthdate);
            driver.setPhoneNumber(phoneNumber);
            driver.setExpedient(expedient);
            driver.setActive(true);
            DriverUser saved = (DriverUser) userRepository.save(driver);
            entityManager.flush();
            return saved;
        } catch (Exception e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password,
            String fullName, String email, Date birthdate,
            String phoneNumber, String education) throws ToursException {
        try {
            TourGuideUser guide = new TourGuideUser();
            guide.setUsername(username);
            guide.setPassword(password);
            guide.setName(fullName);
            guide.setEmail(email);
            guide.setBirthdate(birthdate);
            guide.setPhoneNumber(phoneNumber);
            guide.setEducation(education);
            guide.setActive(true);
            TourGuideUser saved = (TourGuideUser) userRepository.save(guide);
            entityManager.flush();
            return saved;
        } catch (Exception e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        if (!userRepository.existsById(user.getId())) {
            throw new ToursException("Usuario no encontrado");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        if (!user.isActive()) {
            throw new ToursException("El usuario se encuentra desactivado");
        }
        if (user instanceof TourGuideUser) {
            Long count = routeRepository.countRoutesByGuide(user.getId());
            if (count > 0) {
                throw new ToursException("El usuario no puede ser desactivado");
            }
        }
        if (user.getPurchaseList() != null && !user.getPurchaseList().isEmpty()) {
            user.setActive(false);
        } else {
            userRepository.delete(user);
        }
    }

    // ─── STOP ──────────────────────────────────────────

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        return stopRepository.save(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return stopRepository.findByNameStartingWith(name);
    }

    // ─── ROUTE ─────────────────────────────────────────

    @Override
    public Route createRoute(String name, float price, float totalKm,
            int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route route = new Route();
        route.setName(name);
        route.setPrice(price);
        route.setTotalKm(totalKm);
        route.setMaxNumberUsers(maxNumberOfUsers);
        route.setStops(stops);
        return routeRepository.save(route);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.findByPriceLessThan(price);
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty() || !(optUser.get() instanceof DriverUser)) {
            throw new ToursException("No pudo realizarse la asignación");
        }
        Optional<Route> optRoute = routeRepository.findById(idRoute);
        if (optRoute.isEmpty()) {
            throw new ToursException("No pudo realizarse la asignación");
        }
        Route route = optRoute.get();
        route.getDriverList().add((DriverUser) optUser.get());
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty() || !(optUser.get() instanceof TourGuideUser)) {
            throw new ToursException("No pudo realizarse la asignación");
        }
        Optional<Route> optRoute = routeRepository.findById(idRoute);
        if (optRoute.isEmpty()) {
            throw new ToursException("No pudo realizarse la asignación");
        }
        Route route = optRoute.get();
        route.getTourGuideList().add((TourGuideUser) optUser.get());
    }

    // ─── SUPPLIER ──────────────────────────────────────

    @Override
    public Supplier createSupplier(String businessName,
            String authorizationNumber) throws ToursException {
        try {
            Supplier supplier = new Supplier();
            supplier.setBusinessName(businessName);
            supplier.setAuthorizationNumber(authorizationNumber);
            Supplier saved = supplierRepository.save(supplier);
            entityManager.flush();
            return saved;
        } catch (Exception e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    // ─── SERVICE ───────────────────────────────────────

    @Override
    public unlp.info.bd2.model.Service addServiceToSupplier(String name, float price,
            String description, Supplier supplier) throws ToursException {
        try {
            unlp.info.bd2.model.Service service = new unlp.info.bd2.model.Service();
            service.setName(name);
            service.setPrice(price);
            service.setDescription(description);
            service.setSupplier(supplier);
            supplier.getServices().add(service);
            unlp.info.bd2.model.Service saved = serviceRepository.save(service);
            // FIX: flush para detectar constraint de nombre duplicado dentro de la transacción
            entityManager.flush();
            return saved;
        } catch (Exception e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    public unlp.info.bd2.model.Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        unlp.info.bd2.model.Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new ToursException("No existe el producto"));

        service.setPrice(newPrice);
        return service;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<unlp.info.bd2.model.Service> getServiceByNameAndSupplierId(
            String name, Long id) throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    // ─── PURCHASE ──────────────────────────────────────

    @Override
    public Purchase createPurchase(String code, Route route,
            User user) throws ToursException {
        return createPurchase(code, new Date(), route, user);
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route,
            User user) throws ToursException {
        long count = routeRepository.countPurchasesByRoute(route.getId());
        if (count >= route.getMaxNumberUsers()) {
            throw new ToursException("No puede realizarse la compra");
        }
        try {
            Purchase purchase = new Purchase();
            purchase.setCode(code);
            purchase.setDate(date);
            purchase.setRoute(route);
            purchase.setUser(user);
            purchase.setTotalPrice(route.getPrice());
            user.getPurchaseList().add(purchase);
            Purchase saved = purchaseRepository.save(purchase);
            entityManager.flush();
            return saved;
        } catch (Exception e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    public ItemService addItemToPurchase(unlp.info.bd2.model.Service service, int quantity,
            Purchase purchase) throws ToursException {
        ItemService item = new ItemService();
        item.setService(service);
        item.setQuantity(quantity);
        // FIX: Purchase.addItem() ya suma al totalPrice internamente,
        // por eso NO sumamos manualmente acá — evitamos el precio duplicado.
        purchase.addItem(item);
        service.getItemServiceList().add(item);
        return itemServiceRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return purchaseRepository.findByCode(code);
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        if (purchase.getUser() != null) {
            purchase.getUser().getPurchaseList().remove(purchase);
        }
        purchaseRepository.delete(purchase);
        entityManager.flush();
    }

    // ─── REVIEW ────────────────────────────────────────

    @Override
    public Review addReviewToPurchase(int rating, String comment,
            Purchase purchase) throws ToursException {
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setPurchase(purchase);
        purchase.setReview(review);
        return reviewRepository.save(review);
    }

    // ─── HQL ───────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return purchaseRepository.findByUserUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        return userRepository.findUsersSpendingMoreThan(mount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        Pageable pageable = PageRequest.of(0, n);
        return supplierRepository.findTopSuppliersInPurchases(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.countByDateBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        return routeRepository.findByStopsContaining(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return routeRepository.findMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesNotSell() {
        return routeRepository.findRoutesNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        Pageable pageable = PageRequest.of(0, 3);
        return routeRepository.findTop3RoutesWithMaxRating(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public unlp.info.bd2.model.Service getMostDemandedService() {
        Pageable pageable = PageRequest.of(0, 1);
        return serviceRepository.findMostDemandedService(pageable)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.findTourGuidesWithRating1();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoutePurchaseSummaryDTO> getRoutePurchaseSummaries() {
        return routeRepository.findRoutePurchaseSummaries();
    }

}