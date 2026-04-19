package unlp.info.bd2.repositories;

import java.util.List;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

public interface RouteRepository extends BaseRepository<Route, Long> {
    List<Route> findBelowPrice(float price);
    long countPurchasesByRoute(Long routeId);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutesNotSell();
    List<Route> getTop3RoutesWithMaxRating();
    long countRoutesByGuide(Long guideId);
}