package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Review;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Review save(Review review) {
        sessionFactory.getCurrentSession().persist(review);
        return review;
    }

    @Override
    public Optional<Review> findById(Long id) {
        Review review = sessionFactory.getCurrentSession().get(Review.class, id);
        return Optional.ofNullable(review);
    }

    @Override
    public List<Review> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Review", Review.class)
            .list();
    }

    @Override
    public void delete(Review review) {
        sessionFactory.getCurrentSession().remove(review);
    }

}