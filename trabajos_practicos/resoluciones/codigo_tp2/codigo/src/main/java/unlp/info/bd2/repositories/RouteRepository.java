package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import unlp.info.bd2.dto.RoutePurchaseSummaryDTO;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

public interface RouteRepository extends CrudRepository<Route, Long> {

    List<Route> findByPriceLessThan(float price);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.route.id = :routeId")
    long countPurchasesByRoute(@Param("routeId") Long routeId);

    List<Route> findByStopsContaining(Stop stop);

    @Query("SELECT MAX(SIZE(r.stops)) FROM Route r")
    Long findMaxStopOfRoutes();

    @Query("""
        SELECT r FROM Route r
        WHERE r NOT IN (
            SELECT DISTINCT p.route FROM Purchase p
        )
    """)
    List<Route> findRoutesNotSell();

    @Query("""
        SELECT p.route FROM Purchase p
        WHERE p.review IS NOT NULL
        GROUP BY p.route
        ORDER BY AVG(p.review.rating) DESC
    """)
    List<Route> findTop3RoutesWithMaxRating(Pageable pageable);

    @Query("SELECT COUNT(r) FROM Route r JOIN r.tourGuideList g WHERE g.id = :id")
    long countRoutesByGuide(@Param("id") Long guideId);

    @Query("""
        SELECT new unlp.info.bd2.dto.RoutePurchaseSummaryDTO(
            r.name,
            COUNT(p),
            COALESCE(AVG(p.totalPrice), 0)
        )
        FROM Route r
        LEFT JOIN Purchase p ON p.route = r
        GROUP BY r.id, r.name
        ORDER BY r.name
    """)
    List<RoutePurchaseSummaryDTO> findRoutePurchaseSummaries();
}