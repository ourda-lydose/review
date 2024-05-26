package id.ac.ui.cs.advprog.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.service.ReviewService;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;
import org.springframework.web.bind.annotation.RequestHeader;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public CompletableFuture<ResponseEntity<?>> createReview(@RequestBody ReviewDTO reviewDTO) {
        CompletableFuture<Review> future = reviewService.createReview(reviewDTO);

        return future.thenApply(review -> {
            if (review != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(review);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create review");
            }
        });
    }

    // TODO: validate user

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId,
                                          @RequestBody ReviewDTO reviewDTO,
                                          @RequestHeader(name = "X-userid") Integer userId) {

        try {
            Review updatedReview = reviewService.updateReview(reviewId, reviewDTO, userId);
            return ResponseEntity.ok(updatedReview);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable("reviewId") Long reviewId) {
        try {
            Review review = reviewService.getReviewById(reviewId);
            return ResponseEntity.ok(review);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews() {
        List<Review> reviews;
        reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/box/{boxId}")
    public ResponseEntity<List<Review>> getReviewsByBox(@PathVariable String boxId, @RequestParam(value = "rating", required = false) Integer rating) {
        List<Review> reviews;

        if (rating != null) {
            reviews = reviewService.getReviewsByBoxIdAndRating(boxId, rating);
        } else {
            reviews = reviewService.getReviewsByBoxId(boxId);
        }
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{reviewId}/accept")
    public ResponseEntity<?> acceptReview(@PathVariable Long reviewId) {
        try {
            reviewService.acceptReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{reviewId}/reject")
    public ResponseEntity<?> rejectReview(@PathVariable Long reviewId) {
        try {
            reviewService.rejectReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
