package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("""
        SELECT DISTINCT p.user FROM Purchase p
        WHERE p.totalPrice >= :amount
    """)
    List<User> findUsersSpendingMoreThan(@Param("amount") float amount);

    @Query("""
        SELECT DISTINCT g
        FROM Purchase p
        JOIN p.route r
        JOIN r.tourGuideList g
        JOIN p.review rev
        WHERE rev.rating = 1
    """)
    List<TourGuideUser> findTourGuidesWithRating1();
}