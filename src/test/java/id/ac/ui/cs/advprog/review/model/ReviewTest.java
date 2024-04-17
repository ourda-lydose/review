package id.ac.ui.cs.advprog.review.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private Review mockReview;
    private String reviewId;
    private String boxId;
    private int userId;

    @BeforeEach
    void setUp() {
        mockReview = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");
    }

    @Test
    void testCreateReview() {
        Review review = new Review("user-box-1", "box-1", 1, 5, "Box bikin tidak turu");

        assertEquals("user-box-1", review.getReviewId());
        assertEquals("box-1", review.getBoxId());
        assertEquals(1, review.getUserId());
        assertEquals(5, review.getRating());
        assertEquals("Box bikin tidak turu", review.getReviewText());
    }

    @Test
    void testCreateInvalidRating() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Review("user-box-1", "box-1", 1, -1, "negative rating");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Review("user-box-1", "box-1", 1, 6, "positive overflow rating");
        });
    }

    @Test
    void testCreateInvalidReviewText() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Review("user-box-1", "box-1", 1, 5, "");
        });
    }

    @Test
    void testInitialPendingState() {
        assertTrue(mockReview.getStatus() instanceof PendingState);
    }

    void testApproveTransition() {
        mockReview.approve();
        assertTrue(mockReview.getState() instanceof ApprovedState);
    }

    void testRejectTransition() {
        mockReview.reject();
        assertTrue(review.getState() instanceof RejectedState);
    }

    void testEditFromRejectedToPending() {
        review.reject();

        int newRating = 4;
        String newReviewText = "plis tolong eksep";
        review.edit(newRating, newReviewText);

        assertTrue(review.getState() instanceof PendingState);
    }

    void testEditFromAcceptedToPending() {
        review.accept();

        int newRating = 4;
        String newReviewText = "plis tolong eksep lagi;
        review.edit(newRating, newReviewText);

        assertTrue(review.getState() instanceof PendingState);
    }
}