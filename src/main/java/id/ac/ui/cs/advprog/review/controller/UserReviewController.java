package id.ac.ui.cs.advprog.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.service.ReviewService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable("userId") Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}
