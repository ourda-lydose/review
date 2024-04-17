package id.ac.ui.cs.advprog.review.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private Review review;
    private String reviewId;
    private String boxId;
    private int userId;

    @BeforeEach
    void setUp() {
        String reviewId = "user-box-1";
        String boxId = "box-1";
        int userId = 1;
    }

    @Test
    void testCreateReview () {
        review = new Review(reviewId, boxId, userId, 5, "Box bikin tidak turu");

        assertEquals("user-box-1", review.getReviewId());
        assertEquals("box-1", review.getBoxId());
        assertEquals(1, review.getUserId());
        assertEquals(5, review.getRating());
        assertEquals("Box bikin tidak turu", review.getReviewText());
    }

    @Test
    void testCreateInvalidRating () {
        assertThrows(IllegalArgumentException.class, () -> {
            new Review(reviewId, boxId, userId, -1, "Invalid rating (negative)");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Review(reviewId, boxId, userId, 6, "Invalid rating (positive overflow)");
        });
    }

    @Test
    void testCreateInvalidReviewText() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Review(reviewId, boxId, userId, 3, "");
        });
    }
}