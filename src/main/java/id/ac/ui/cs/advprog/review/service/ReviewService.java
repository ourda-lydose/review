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
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Service
public class ReviewService {
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());

    private final ReviewRepository reviewRepository;
    @Value("${auth.service.base-url}")
    private String authServiceBaseUrl;

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
        return optionalReview.orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " not found"));
    }

    // reference: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
    @Async
    public CompletableFuture<Review> createReview(ReviewDTO reviewDTO) {
        Review review = new Review(reviewDTO.getBoxId(), reviewDTO.getUserId(), reviewDTO.getRating(), reviewDTO.getReviewText());
        Review savedReview = reviewRepository.save(review);
        return CompletableFuture.completedFuture(savedReview);
    }

    // TODO: make sure only admin can access this (implement on api gateway)
    // TODO: return error/text if state not compatible
    public void acceptReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " notTTTTTTT found"));

        review.approveReview();
        reviewRepository.save(review);
    }

    // TODO: make sure only admin can access this (implement on api gateway)
    // TODO: return error/text if state not compatible
    public void rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " notTTTTTTT found"));

        review.rejectReview();
        reviewRepository.save(review);
    }
}
