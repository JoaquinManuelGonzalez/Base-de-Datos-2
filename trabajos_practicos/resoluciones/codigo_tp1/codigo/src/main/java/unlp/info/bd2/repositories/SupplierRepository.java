package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.Supplier;

public interface SupplierRepository extends BaseRepository<Supplier, Long> {
    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);
    List<Supplier> getTopNSuppliersInPurchases(int n);
}
