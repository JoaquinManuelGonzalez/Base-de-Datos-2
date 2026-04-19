package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;

@Repository
public class SupplierRepositoryImpl implements SupplierRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Supplier save(Supplier supplier) {
        sessionFactory.getCurrentSession().persist(supplier);
        return supplier;
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        Supplier supplier = sessionFactory.getCurrentSession().get(Supplier.class, id);
        return Optional.ofNullable(supplier);
    }

    @Override
    public List<Supplier> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Supplier", Supplier.class)
            .list();
    }

    @Override
    public void delete(Supplier supplier) {
        sessionFactory.getCurrentSession().remove(supplier);
    }

    @Override
    public Optional<Supplier> findByAuthorizationNumber(String authorizationNumber) {
        Supplier supplier = sessionFactory.getCurrentSession()
            .createQuery(
                "FROM Supplier s WHERE s.authorizationNumber = :authNum",
                Supplier.class)
            .setParameter("authNum", authorizationNumber)
            .uniqueResult();
        return Optional.ofNullable(supplier);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        // navega: Supplier <- Service <- ItemService <- Purchase
        // cuenta cuántas veces aparece cada supplier en compras
        // y ordena de mayor a menor
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT s.supplier FROM Service s " +
                "JOIN s.itemServiceList item " +
                "JOIN item.purchase p " +
                "GROUP BY s.supplier " +
                "ORDER BY COUNT(p) DESC", Supplier.class)
            .setMaxResults(n)
            .list();
    }

}
