package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Purchase save(Purchase purchase) {
        sessionFactory.getCurrentSession().persist(purchase);
        return purchase;
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        Purchase purchase = sessionFactory.getCurrentSession().get(Purchase.class, id);
        return Optional.ofNullable(purchase);
    }

    @Override
    public List<Purchase> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Purchase", Purchase.class)
            .list();
    }

    @Override
    public void delete(Purchase purchase) {
        sessionFactory.getCurrentSession().remove(purchase);
    }

    @Override
    public Optional<Purchase> findByCode(String code) {
        Purchase purchase = sessionFactory.getCurrentSession()
            .createQuery("FROM Purchase p WHERE p.code = :code", Purchase.class)
            .setParameter("code", code)
            .uniqueResult();
        return Optional.ofNullable(purchase);
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT p FROM Purchase p " +
                "WHERE p.user.username = :username", Purchase.class)
            .setParameter("username", username)
            .list();
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT COUNT(p) FROM Purchase p " +
                "WHERE p.date >= :start AND p.date <= :end", Long.class)
            .setParameter("start", start)
            .setParameter("end", end)
            .uniqueResult();
    }

}
