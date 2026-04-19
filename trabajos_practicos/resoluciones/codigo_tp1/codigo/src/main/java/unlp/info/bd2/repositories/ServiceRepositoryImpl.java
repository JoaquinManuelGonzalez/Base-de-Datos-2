package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Service;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Service save(Service service) {
        sessionFactory.getCurrentSession().persist(service);
        return service;
    }

    @Override
    public Optional<Service> findById(Long id) {
        Service service = sessionFactory.getCurrentSession().get(Service.class, id);
        return Optional.ofNullable(service);
    }

    @Override
    public List<Service> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Service", Service.class)
            .list();
    }

    @Override
    public void delete(Service service) {
        sessionFactory.getCurrentSession().remove(service);
    }

    @Override
    public Optional<Service> findByNameAndSupplierId(String name, Long supplierId) {
        Service service = sessionFactory.getCurrentSession()
            .createQuery(
                "FROM Service s WHERE s.name = :name " +
                "AND s.supplier.id = :supplierId", Service.class)
            .setParameter("name", name)
            .setParameter("supplierId", supplierId)
            .uniqueResult();
        return Optional.ofNullable(service);
    }

    @Override
    public Service getMostDemandedService() {
        // el servicio que más veces fue incluido en compras
        // teniendo en cuenta la cantidad (quantity) de cada ItemService
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT item.service FROM ItemService item " +
                "GROUP BY item.service " +
                "ORDER BY SUM(item.quantity) DESC",
                Service.class)
            .setMaxResults(1)
            .uniqueResult();
    }

}
