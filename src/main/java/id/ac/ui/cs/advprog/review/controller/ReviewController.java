package id.ac.ui.cs.advprog.review.controller;

import id.ac.ui.cs.advprog.review.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.service.ReviewService;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.*;

@RestController
@RequestMapping("/api/reviews")
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

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@RequestParam(value = "boxId", required = false) String boxId) {
        List<Review> reviews;

        if (boxId != null) {
            reviews = reviewService.getReviewsByBoxId(boxId);
        } else {
            reviews = reviewService.getAllReviews();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable Long reviewId) {
        try {
            Review review = reviewService.getReviewById(reviewId);
            return ResponseEntity.ok(review);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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


//    @PostMapping
//    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String token, @RequestBody ReviewDTO reviewDTO) {
//        System.out.println("Token received: " + token);
//        User user = reviewService.getAuthenticatedUser(token);
//
//        Review review = reviewService.addReview(reviewDTO, user.getId());
//
//        return ResponseEntity.ok(review);
//    }

//    @PostMapping
//    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String token, @RequestBody ReviewDTO reviewDTO) {
//        System.out.println("Token received: " + token);
//        User user = reviewService.getAuthenticatedUser(token);
//
//        // Set the default status to PENDING if not provided in the request
//        if (reviewDTO.getStatus() == null || reviewDTO.getStatus().isEmpty()) {
//            reviewDTO.setStatus("PENDING");
//        }
//
//        // Create and add the review using the service method
//        Review review = reviewService.addReview(reviewDTO, user.getId());
//
//        return ResponseEntity.ok(review);
//    }



//    @PutMapping("/{reviewId}")
//    public ResponseEntity<?> updateReview(@RequestHeader("Authorization") String token, @PathVariable("reviewId") Long reviewId, @RequestBody ReviewDTO reviewDTO) {
//        Integer userId = reviewService.getAuthenticatedUserId(token);
//
//        Review updatedReview = reviewService.updateReview(reviewId, reviewDTO, userId);
//
//        return ResponseEntity.ok(updatedReview);
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<?> deleteReview(@RequestHeader("Authorization") String token, @PathVariable("reviewId") Long reviewId) {
//        Integer userId = reviewService.getAuthenticatedUserId(token);
//
//        reviewService.deleteReview(reviewId, userId);
//
//        return ResponseEntity.ok().build();
//    }
}
