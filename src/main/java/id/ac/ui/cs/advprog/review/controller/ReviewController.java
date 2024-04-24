package id.ac.ui.cs.advprog.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.service.ReviewService;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO reviewDTO) {
        Integer userId = reviewService.getAuthenticatedUserId();

        Review review = reviewService.addReview(reviewDTO, userId);

        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") String reviewId, @RequestBody ReviewDTO reviewDTO) {
        Integer userId = reviewService.getAuthenticatedUserId();

        Review updatedReview = reviewService.updateReview(reviewId, reviewDTO, userId);

        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") String reviewId) {
        Integer userId = reviewService.getAuthenticatedUserId();

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok().build();
    }
}
