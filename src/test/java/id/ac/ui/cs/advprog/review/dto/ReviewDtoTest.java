package id.ac.ui.cs.advprog.review.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReviewDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testValidReviewDTO() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setBoxId("28ab0b8c-b580-46db-8308-a758e4bac948");
        reviewDTO.setUserId(1);
        reviewDTO.setRating(5);
        reviewDTO.setReviewText("Great product!");

        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidRating() {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setBoxId("28ab0b8c-b580-46db-8308-a758e4bac948");
        reviewDTO.setUserId(1);
        reviewDTO.setReviewText("Great product!");
        reviewDTO.setRating(6);

        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);
        assertEquals(1, violations.size());
        assertEquals("Rating must be at most 5", violations.iterator().next().getMessage());
    }

    @Test
    void testEmptyReviewText() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setBoxId("28ab0b8c-b580-46db-8308-a758e4bac948");
        reviewDTO.setUserId(1);
        reviewDTO.setRating(5);
        reviewDTO.setReviewText("");

        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Review text can't be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testGettersAndSetters() {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setBoxId("28ab0b8c-b580-46db-8308-a758e4bac948");
        reviewDTO.setUserId(1);
        reviewDTO.setRating(5);
        reviewDTO.setReviewText("Great product!");
        reviewDTO.setStatusString("PENDING");

        assertEquals("28ab0b8c-b580-46db-8308-a758e4bac948", reviewDTO.getBoxId());
        assertEquals(1, reviewDTO.getUserId());
        assertEquals(5, reviewDTO.getRating());
        assertEquals("Great product!", reviewDTO.getReviewText());
        assertEquals("PENDING", reviewDTO.getStatusString());
    }
}
