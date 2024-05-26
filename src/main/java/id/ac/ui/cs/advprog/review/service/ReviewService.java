package id.ac.ui.cs.advprog.review.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.review.model.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.model.PendingState;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private String NOT_FOUND = "NOT FOUND";
    private String INVALID_ID_PREFIX = "Review with ID";

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // repository-ish
    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    public List<Review> getReviewsByBoxId(String boxId) {
        return reviewRepository.findByBoxId(boxId);
    }

    public Review getReviewById(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        return optionalReview.orElseThrow(() -> new NoSuchElementException(INVALID_ID_PREFIX + reviewId + NOT_FOUND));
    }

    public List<Review> getReviewsByBoxIdAndRating(String boxId, int rating) {
        return reviewRepository.findByBoxIdAndRating(boxId, rating);
    }

    // reference: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
    @Async
    public CompletableFuture<Review> createReview(ReviewDTO reviewDTO) {
        Review review = new Review(reviewDTO.getBoxId(), reviewDTO.getUserId(), reviewDTO.getRating(), reviewDTO.getReviewText());
        Review savedReview = reviewRepository.save(review);
        return CompletableFuture.completedFuture(savedReview);
    }

    public Review updateReview(Long reviewId, ReviewDTO reviewDTO, Integer userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException(INVALID_ID_PREFIX + reviewId + NOT_FOUND));

        if (userId != -1 && !review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not the author of the review.");
        }

        int rating = reviewDTO.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        String reviewText = reviewDTO.getReviewText();
        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new IllegalArgumentException("Review text cannot be empty.");
        }

        review.updateReview(reviewDTO.getRating(), reviewDTO.getReviewText());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException(INVALID_ID_PREFIX + reviewId + NOT_FOUND));
        reviewRepository.delete(review);
    }

    public void acceptReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException(INVALID_ID_PREFIX + reviewId + NOT_FOUND));

        review.approveReview();
        reviewRepository.save(review);
    }

    public void rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException(INVALID_ID_PREFIX + reviewId + NOT_FOUND));

        review.rejectReview();
        reviewRepository.save(review);
    }
}
