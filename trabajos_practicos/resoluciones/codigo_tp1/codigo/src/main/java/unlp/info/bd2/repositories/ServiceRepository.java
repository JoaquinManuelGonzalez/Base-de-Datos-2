package unlp.info.bd2.repositories;

import java.util.Optional;

import unlp.info.bd2.model.Service;

public interface ServiceRepository  extends BaseRepository<Service, Long> {
    Optional<Service> findByNameAndSupplierId(String name, Long supplierId);
    Service getMostDemandedService();
}
