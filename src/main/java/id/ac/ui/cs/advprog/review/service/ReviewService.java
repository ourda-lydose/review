package id.ac.ui.cs.advprog.review.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.review.model.StatusEnum;
import id.ac.ui.cs.advprog.review.model.User;
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

    private final RestTemplate restTemplate;
    private final ReviewRepository reviewRepository;
    @Value("${auth.service.base-url}")
    private String authServiceBaseUrl;

    @Autowired
    public ReviewService(RestTemplate restTemplate, ReviewRepository reviewRepository) {
        this.restTemplate = restTemplate;
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

    // TODO: make sure yang bisa accept cuma ADMIN
    public void acceptReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " notTTTTTTT found"));

        review.approveReview();
        reviewRepository.save(review);
    }



//    public Review updateReview(Long reviewId, ReviewDTO reviewDTO, Integer userId) {
//        Review existingReview = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
//
//        if (!existingReview.getUserId().equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the review");
//        }
//
//        existingReview.setRating(reviewDTO.getRating());
//        existingReview.setReviewText(reviewDTO.getReviewText());
//        return reviewRepository.save(existingReview);
//    }

//    public void deleteReview(Long reviewId, Integer userId) {
////        int userId = getAuthenticatedUserId(token);
//
//        Review existingReview = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
//
//        if (!existingReview.getUserId().equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the review");
//        }
//        reviewRepository.delete(existingReview);
//    }

    // implementasi asyc, tp blm coba biar terintegrasi lebih si, yg penting ud bikin
//    @Async
//    public Future<Review> addReviewAsync(ReviewDTO reviewDTO, Integer userId) {
//        Review review = new Review();
//        review.setBoxId(reviewDTO.getBoxId());
//        review.setUserId(userId);
//        review.setRating(reviewDTO.getRating());
//        review.setReviewText(reviewDTO.getReviewText());
//        review.setStatusString(reviewDTO.getStatus());
//
//        review.setStatus(new PendingState(review));
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Review savedReview = reviewRepository.save(review);
//        return new AsyncResult<>(savedReview);
//    }

//    public Integer getAuthenticatedUserId(String token) {
//        logger.info("ini token: " + token);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<User> response = restTemplate.exchange(
//                authServiceBaseUrl + "/users/me",
//                HttpMethod.GET,
//                entity,
//                User.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody().getId();
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
//        }
//    }

//    public User getAuthenticatedUser(String token) {
//        logger.info("Token: " + token);
//
//        if (token == null || token.isEmpty()) {
//            throw new IllegalArgumentException("Token is null or empty");
//        }
//
//        logger.info("sampe sini");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", token);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//
//        logger.info("Headers: " + headers);
//
//        ResponseEntity<User> response = restTemplate.exchange(
//                authServiceBaseUrl + "/users/me",
//                HttpMethod.GET,
//                entity,
//                User.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
//        }
//    }

//    public User getAuthenticatedUser(String token) {
////        logger.info("Token: " + token);
//
//        if (token == null || token.isEmpty()) {
//            throw new IllegalArgumentException("Token is null or empty");
//        }
//
////      logger.info("sampe sini");
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", token);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        logger.info("Headers: " + headers);
//
//        ResponseEntity<User> response = restTemplate.exchange(
//                "http://localhost:8080/users/me",
//                HttpMethod.GET,
//                entity,
//                User.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            User user = response.getBody();
//            if (user != null) {
//                return user;
//            } else {
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response body");
//            }
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
//        }
//    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
