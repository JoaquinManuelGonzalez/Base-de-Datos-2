package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Route save(Route route) {
        sessionFactory.getCurrentSession().persist(route);
        return route;
    }

    @Override
    public Optional<Route> findById(Long id) {
        Route route = sessionFactory.getCurrentSession().get(Route.class, id);
        return Optional.ofNullable(route);
    }

    @Override
    public List<Route> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Route", Route.class)
            .list();
    }

    @Override
    public void delete(Route route) {
        sessionFactory.getCurrentSession().remove(route);
    }

    @Override
    public List<Route> findBelowPrice(float price) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Route r WHERE r.price < :price", Route.class)
            .setParameter("price", price)
            .list();
    }

    @Override
    public long countPurchasesByRoute(Long routeId) {
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT COUNT(p) FROM Purchase p WHERE p.route.id = :routeId",
                Long.class)
            .setParameter("routeId", routeId)
            .uniqueResult();
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // rutas que contienen la parada especificada
        // navega la relación ManyToMany Route -> stops
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT DISTINCT r FROM Route r " +
                "JOIN r.stops s " +
                "WHERE s = :stop", Route.class)
            .setParameter("stop", stop)
            .list();
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // cantidad de stops del recorrido con más paradas
        Number maxStops = sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT MAX(SIZE(r.stops)) FROM Route r",
                Number.class)
            .uniqueResult();
        return maxStops == null ? 0L : maxStops.longValue();
    }

    @Override
    public List<Route> getRoutesNotSell() {
        // rutas que no tienen ninguna compra asociada
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT r FROM Route r " +
                "WHERE r NOT IN (" +
                "   SELECT DISTINCT p.route FROM Purchase p" +
                ")", Route.class)
            .list();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return this.sessionFactory.getCurrentSession()
            .createQuery("select p.route from Purchase p where p.review is not null " +
                        "group by p.route order by avg(p.review.rating) desc", Route.class)
            .setMaxResults(3)
            .getResultList();
    }

    @Override
    public long countRoutesByGuide(Long guideId) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(r) FROM Route r JOIN r.tourGuideList g WHERE g.id = :id", Long.class)
            .setParameter("id", guideId)
            .uniqueResult();
    }
}
