package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;

import unlp.info.bd2.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    
}