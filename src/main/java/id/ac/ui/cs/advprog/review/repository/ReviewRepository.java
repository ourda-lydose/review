package id.ac.ui.cs.advprog.review.repository;

import id.ac.ui.cs.advprog.review.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, String> {
    Optional<Review> findReviewByReviewId(String reviewId);
    Optional<Review> findReviewByUserIdAndBoxId(int userId, String boxId);
}