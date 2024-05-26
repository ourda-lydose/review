package id.ac.ui.cs.advprog.review.controller;

import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview_ReturnsCreatedReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Review review = new Review();
        CompletableFuture<Review> future = CompletableFuture.completedFuture(review);

        when(reviewService.createReview(any())).thenReturn(future);

        ResponseEntity<?> responseEntity = reviewController.createReview(reviewDTO).join();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(review, responseEntity.getBody());
    }

    @Test
    void createReview_ReturnsInternalServerError() {
        ReviewDTO reviewDTO = new ReviewDTO();
        CompletableFuture<Review> future = CompletableFuture.completedFuture(null);

        when(reviewService.createReview(any())).thenReturn(future);

        ResponseEntity<?> responseEntity = reviewController.createReview(reviewDTO).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to create review", responseEntity.getBody());
    }

    @Test
    void updateReview_ReturnsUpdatedReview() {
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        Review review = new Review();

        when(reviewService.updateReview(eq(reviewId), any(), anyInt())).thenReturn(review);

        ResponseEntity<?> responseEntity = reviewController.updateReview(reviewId, reviewDTO, 1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(review, responseEntity.getBody());
    }

    @Test
    void updateReview_ReturnsNotFound() {
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();

        when(reviewService.updateReview(eq(reviewId), any(), anyInt())).thenThrow(new NoSuchElementException());

        ResponseEntity<?> responseEntity = reviewController.updateReview(reviewId, reviewDTO, 1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void updateReview_ReturnsForbidden() {
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();

        when(reviewService.updateReview(eq(reviewId), any(), anyInt())).thenThrow(new IllegalArgumentException());

        ResponseEntity<?> responseEntity = reviewController.updateReview(reviewId, reviewDTO, 1);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void updateReview_ReturnsInternalServerError() {
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();

        when(reviewService.updateReview(eq(reviewId), any(), anyInt())).thenThrow(new RuntimeException());

        ResponseEntity<?> responseEntity = reviewController.updateReview(reviewId, reviewDTO, 1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void deleteReview_ReturnsOk() {
        Long reviewId = 1L;

        ResponseEntity<?> responseEntity = reviewController.deleteReview(reviewId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteReview_ReturnsNotFound() {
        Long reviewId = 1L;

        doThrow(new NoSuchElementException()).when(reviewService).deleteReview(eq(reviewId));

        ResponseEntity<?> responseEntity = reviewController.deleteReview(reviewId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getReviewById_ReturnsReview() {
        Long reviewId = 1L;
        Review review = new Review();

        when(reviewService.getReviewById(eq(reviewId))).thenReturn(review);

        ResponseEntity<?> responseEntity = reviewController.getReviewById(reviewId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(review, responseEntity.getBody());
    }

    @Test
    void getReviewById_ReturnsNotFound() {
        Long reviewId = 1L;

        when(reviewService.getReviewById(eq(reviewId))).thenThrow(new NoSuchElementException());

        ResponseEntity<?> responseEntity = reviewController.getReviewById(reviewId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getReviews_ReturnsReviews() {
        List<Review> reviews = Collections.singletonList(new Review());

        when(reviewService.getAllReviews()).thenReturn(reviews);

        ResponseEntity<List<Review>> responseEntity = reviewController.getReviews();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reviews, responseEntity.getBody());
    }

    @Test
    void getReviewsByBox_ReturnsReviews() {
        String boxId = "28ab0b8c-b580-46db-8308-a758e4bac948";
        Integer rating = 5;
        List<Review> reviews = Collections.singletonList(new Review());

        when(reviewService.getReviewsByBoxIdAndRating(eq(boxId), eq(rating))).thenReturn(reviews);

        ResponseEntity<List<Review>> responseEntity = reviewController.getReviewsByBox(boxId, rating);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reviews, responseEntity.getBody());
    }

    @Test
    void getReviewsByBox_ReturnsReviewsWithoutRating() {
        String boxId = "28ab0b8c-b580-46db-8308-a758e4bac948";
        List<Review> reviews = Collections.singletonList(new Review());

        when(reviewService.getReviewsByBoxId(eq(boxId))).thenReturn(reviews);

        ResponseEntity<List<Review>> responseEntity = reviewController.getReviewsByBox(boxId, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reviews, responseEntity.getBody());
    }

    @Test
    void acceptReview_ReturnsOk() {
        Long reviewId = 1L;

        ResponseEntity<?> responseEntity = reviewController.acceptReview(reviewId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void acceptReview_ReturnsNotFound() {
        Long reviewId = 1L;

        doThrow(new NoSuchElementException()).when(reviewService).acceptReview(eq(reviewId));

        ResponseEntity<?> responseEntity = reviewController.acceptReview(reviewId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void rejectReview_ReturnsOk() {
        Long reviewId = 1L;

        ResponseEntity<?> responseEntity = reviewController.rejectReview(reviewId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void rejectReview_ReturnsNotFound() {
        Long reviewId = 1L;

        doThrow(new NoSuchElementException()).when(reviewService).rejectReview(eq(reviewId));

        ResponseEntity<?> responseEntity = reviewController.rejectReview(reviewId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
