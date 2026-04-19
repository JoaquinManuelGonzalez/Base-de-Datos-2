package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;
import unlp.info.bd2.model.Purchase;

import java.util.List;
import java.util.Optional;
import java.util.Date;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    Optional<Purchase> findByCode(String code);

    List<Purchase> findByUserUsername(String username);

    long countByDateBetween(Date start, Date end);
}
