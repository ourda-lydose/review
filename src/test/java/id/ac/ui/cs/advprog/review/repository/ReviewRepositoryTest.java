import static org.junit.jupiter.api.Assertions.*;


import id.ac.ui.cs.advprog.review.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testFindById() {
        Review review = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
        reviewRepository.addReview(review);

        Optional<Review> searchedReview = reviewRepository.findById(review.getReviewId());

        assertTrue(searchedReview.isPresent());
        assertEquals(review.getReviewId(), searchedReview.get().getReviewText());
    }

    @Test
    public void testFindByIdNotFound() {
        Review review = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
        reviewRepository.addReview(review);

        Optional<Review> searchedReview = reviewRepository.findById("user-box-2");

        assertNull(searchedReview);
    }

    @Test
    public void testAddMultipleReviews() {
        Review review1 = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
        Review review2 = new Review("user-box-2", "box-1", 2, 5, "Box bikin tidak turu");

        reviewRepository.addReview(review1);
        reviewRepository.addReview(review2);

        assertEquals(2, reviewRepository.count());

        Optional<Review> foundReview1 = reviewRepository.findById(review1.getId());
        Optional<Review> foundReview2 = reviewRepository.findById(review2.getId());

        assertTrue(foundReview1.isPresent());
        assertEquals(review1.getRating(), foundReview1.get().getRating());
        assertEquals(review1.getReviewText(), foundReview1.get().getReviewText());

        assertTrue(foundReview2.isPresent());
        assertEquals(review2.getRating(), foundReview1.get().getRating());
        assertEquals(review2.getReviewText(), foundReview2.get().getReviewText());
    }

    @Test
    void TestAddMultipleReviewWithSameUserId() {
        Review review1 = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
        Review review2 = new Review("user-box-2", "box-1", 1, 4, "Box bikin tidak turu versi 2");
        reviewRepository.addReview(review1);
        reviewRepository.addReview(review2);

        assertEquals(1, reviewRepository.count());

        Optional<Review> foundReview1 = reviewRepository.findById(review1.getId());
        assertTrue(foundReview1.isPresent());
        assertEquals(review2.getReviewText(), review1.getReviewText());
    }

    @Test
    public void testDeleteReview() {
        Review review = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
        reviewRepository.addReview(review.getReviewId());
        reviewRepository.deleteReview(review.getReviewId());

        Optional<Review> deletedReview = reviewRepository.findById(review.getReviewId());
        assertNull(deletedReview);
    }

    @Test
    public void testDeleteReviewNotFound() {
        Review review = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
        reviewRepository.addReview(review.getReviewId());
        reviewRepository.deleteReview("user-box-2");

        Optional<Review> foundReview = reviewRepository.findById(review.getReviewId());
        assertTrue(foundReview.isPresent());
    }
}