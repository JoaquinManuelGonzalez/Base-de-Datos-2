package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Stop;

@Repository
public class StopRepositoryImpl implements StopRepository {
    
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Stop save(Stop stop) {
        sessionFactory.getCurrentSession().persist(stop);
        return stop;
    }

    @Override
    public List<Stop> findByNameStart(String name) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Stop s WHERE s.name LIKE :name", Stop.class)
            .setParameter("name", name + "%")
            .list();
    }

    @Override
    public Optional<Stop> findById(Long id) {
        return Optional.ofNullable(
            sessionFactory.getCurrentSession().get(Stop.class, id));
    }

    @Override
    public List<Stop> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Stop", Stop.class).list();
    }

    @Override
    public void delete(Stop stop) {
        sessionFactory.getCurrentSession().remove(stop);
    }
}
