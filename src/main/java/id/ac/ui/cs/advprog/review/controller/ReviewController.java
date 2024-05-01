package id.ac.ui.cs.advprog.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.service.ReviewService;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;

import java.util.logging.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());

    public ReviewController() {
        // Set logging level to INFO
        logger.setLevel(Level.INFO);
    }



    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String token, @RequestBody ReviewDTO reviewDTO) {
        System.out.println("Token received: " + token);
        Integer userId = reviewService.getAuthenticatedUserId(token);

        Review review = reviewService.addReview(reviewDTO, userId);

        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@RequestHeader("Authorization") String token, @PathVariable("reviewId") Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        Integer userId = reviewService.getAuthenticatedUserId(token);

        Review updatedReview = reviewService.updateReview(reviewId, reviewDTO, userId);

        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@RequestHeader("Authorization") String token, @PathVariable("reviewId") Long reviewId) {
        Integer userId = reviewService.getAuthenticatedUserId(token);

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok().build();
    }
}
