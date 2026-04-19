package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import unlp.info.bd2.model.Supplier;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {

    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Query("""
        SELECT s.supplier FROM Service s
        JOIN s.itemServiceList item
        JOIN item.purchase p
        GROUP BY s.supplier
        ORDER BY COUNT(p) DESC
    """)
    List<Supplier> findTopSuppliersInPurchases(Pageable pageable);
}