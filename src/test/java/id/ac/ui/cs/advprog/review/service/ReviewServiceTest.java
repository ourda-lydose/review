package id.ac.ui.cs.advprog.review.service;

import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review mockReview;

    @BeforeEach
    void setUp() {
        mockReview = new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 5, "Great product!");
        mockReview.setReviewId(1L);
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(mockReview));

        List<Review> reviews = reviewService.getAllReviews();

        assertFalse(reviews.isEmpty());
        assertEquals(1, reviews.size());
    }

    @Test
    void testGetReviewsByBoxId() {
        when(reviewRepository.findByBoxId(mockReview.getBoxId().toString())).thenReturn(List.of(mockReview));

        List<Review> reviews = reviewService.getReviewsByBoxId(mockReview.getBoxId().toString());

        assertFalse(reviews.isEmpty());
        assertEquals(1, reviews.size());
    }

    @Test
    void testGetReviewById() {
        when(reviewRepository.findById(mockReview.getReviewId())).thenReturn(Optional.of(mockReview));

        Review review = reviewService.getReviewById(mockReview.getReviewId());

        assertNotNull(review);
        assertEquals(mockReview, review);
    }

    @Test
    void testGetReviewsByBoxIdAndRating() {
        when(reviewRepository.findByBoxIdAndRating(mockReview.getBoxId().toString(), mockReview.getRating())).thenReturn(List.of(mockReview));

        List<Review> reviews = reviewService.getReviewsByBoxIdAndRating(mockReview.getBoxId().toString(), mockReview.getRating());

        assertFalse(reviews.isEmpty());
        assertEquals(1, reviews.size());
    }

    @Test
    void testCreateReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setBoxId(mockReview.getBoxId().toString());
        reviewDTO.setUserId(mockReview.getUserId());
        reviewDTO.setRating(mockReview.getRating());
        reviewDTO.setReviewText(mockReview.getReviewText());

        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);

        Review createdReview = reviewService.createReview(reviewDTO).join();

        assertNotNull(createdReview);
        assertEquals(mockReview, createdReview);
    }

    @Test
    void testUpdateReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(4);
        reviewDTO.setReviewText("Updated review text");

        when(reviewRepository.findById(mockReview.getReviewId())).thenReturn(Optional.of(mockReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);

        Review updatedReview = reviewService.updateReview(mockReview.getReviewId(), reviewDTO, 1);

        assertNotNull(updatedReview);
        assertEquals(mockReview.getRating(), updatedReview.getRating());
        assertEquals(reviewDTO.getReviewText(), updatedReview.getReviewText());
    }

    @Test
    void testDeleteReview() {
        when(reviewRepository.findById(mockReview.getReviewId())).thenReturn(Optional.of(mockReview));

        assertDoesNotThrow(() -> reviewService.deleteReview(mockReview.getReviewId()));
    }

    @Test
    void testAcceptReview() {
        when(reviewRepository.findById(mockReview.getReviewId())).thenReturn(Optional.of(mockReview));

        assertDoesNotThrow(() -> reviewService.acceptReview(mockReview.getReviewId()));
    }

    @Test
    void testRejectReview() {
        when(reviewRepository.findById(mockReview.getReviewId())).thenReturn(Optional.of(mockReview));

        assertDoesNotThrow(() -> reviewService.rejectReview(mockReview.getReviewId()));
    }

    @Test
    void testUpdateReview_UserNotAuthor() {
        Long reviewId = 1L;
        Integer userId = 2;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(5);
        reviewDTO.setReviewText("Updated review text");
        Review mockReview = new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 5, "Great product!");
        mockReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));

        assertThrows(IllegalArgumentException.class, () -> reviewService.updateReview(reviewId, reviewDTO, userId));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testUpdateReview_InvalidRating() {
        Long reviewId = 1L;
        Integer userId = 1;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(6);
        reviewDTO.setReviewText("Updated review text");
        Review mockReview = new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 5, "Great product!");
        mockReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));

        assertThrows(IllegalArgumentException.class, () -> reviewService.updateReview(reviewId, reviewDTO, userId));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testUpdateReview_EmptyReviewText() {
        Long reviewId = 1L;
        Integer userId = 1;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(4);
        reviewDTO.setReviewText("");
        Review mockReview = new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 5, "Great product!");
        mockReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(mockReview));

        assertThrows(IllegalArgumentException.class, () -> reviewService.updateReview(reviewId, reviewDTO, userId));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testDeleteReview_ReviewNotFound() {
        Long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reviewService.deleteReview(reviewId));
        verify(reviewRepository, never()).delete(any());
    }

    @Test
    void testAcceptReview_ReviewNotFound() {
        Long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reviewService.acceptReview(reviewId));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testRejectReview_ReviewNotFound() {
        Long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> reviewService.rejectReview(reviewId));
        verify(reviewRepository, never()).save(any());
    }
}
