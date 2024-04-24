package id.ac.ui.cs.advprog.review.service;

import id.ac.ui.cs.advprog.review.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import id.ac.ui.cs.advprog.review.dto.ReviewDTO;
import id.ac.ui.cs.advprog.review.model.Review;
import id.ac.ui.cs.advprog.review.model.PendingState;
import id.ac.ui.cs.advprog.review.repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    private final RestTemplate restTemplate;
    private final ReviewRepository reviewRepository;
    @Value("${auth.service.url}") //implementasi komunikasi antarservice
    private String authServiceUrl;

    @Autowired
    public ReviewService(RestTemplate restTemplate, ReviewRepository reviewRepository) {
        this.restTemplate = restTemplate;
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    public Integer getAuthenticatedUserId() {
        ResponseEntity<User> response = restTemplate.getForEntity(authServiceUrl + "/users/me", User.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getId();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
    }

    public boolean authenticateUser(String token) {
        ResponseEntity<User> response = restTemplate.getForEntity(authServiceUrl + "/users/me", User.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public Review addReview(ReviewDTO reviewDTO, int userId) {
        if (!authenticateUser(String.valueOf(userId))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Review review = new Review(null, reviewDTO.getBoxId(), userId, reviewDTO.getRating(), reviewDTO.getReviewText());
        review.setStatus(new PendingState(review));
        return reviewRepository.save(review);
    }

    public Review updateReview(String reviewId, ReviewDTO reviewDTO, Integer userId) {
        if (!authenticateUser(String.valueOf(userId))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Review existingReview = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if (!existingReview.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the review");
        }

        existingReview.setRating(reviewDTO.getRating());
        existingReview.setReviewText(reviewDTO.getReviewText());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(String reviewId, Integer userId) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if (!existingReview.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the review");
        }

        reviewRepository.delete(existingReview);
    }
}
