package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> getUserSpendingMoreThan(float mount);
    List<TourGuideUser> getTourGuidesWithRating1();
}