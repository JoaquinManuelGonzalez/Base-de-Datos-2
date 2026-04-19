package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.Purchase;

public interface PurchaseRepository extends BaseRepository<Purchase, Long> {
    Optional<Purchase> findByCode(String code);
    List<Purchase> getAllPurchasesOfUsername(String username);
    long getCountOfPurchasesBetweenDates(Date start, Date end);

}
