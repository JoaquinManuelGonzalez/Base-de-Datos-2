package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User save(User user) {
        sessionFactory.getCurrentSession().persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = sessionFactory.getCurrentSession().get(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM User", User.class)
            .list();
    }

    @Override
    public void delete(User user) {
        sessionFactory.getCurrentSession().remove(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user = sessionFactory.getCurrentSession()
            .createQuery("FROM User u WHERE u.username = :username", User.class)
            .setParameter("username", username)
            .uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        // busca usuarios que tengan al menos una compra
        // con totalPrice >= mount
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT DISTINCT p.user FROM Purchase p " +
                "WHERE p.totalPrice >= :mount", User.class)
            .setParameter("mount", mount)
            .list();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.sessionFactory.getCurrentSession()
            .createQuery("select distinct g from Route r join r.tourGuideList g, Purchase p "
                    + "where p.route = r and p.review.rating = 1", TourGuideUser.class)
            .getResultList();
    }

}