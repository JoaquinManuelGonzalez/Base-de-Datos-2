package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.ItemService;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemServiceRepositoryImpl implements ItemServiceRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ItemService save(ItemService item) {
        sessionFactory.getCurrentSession().persist(item);
        return item;
    }

    @Override
    public Optional<ItemService> findById(Long id) {
        return Optional.ofNullable(
            sessionFactory.getCurrentSession().get(ItemService.class, id));
    }

    @Override
    public List<ItemService> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM ItemService", ItemService.class)
            .list();
    }

    @Override
    public void delete(ItemService item) {
        sessionFactory.getCurrentSession().remove(item);
    }
}
