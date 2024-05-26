package id.ac.ui.cs.advprog.review.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private Review mockReview;

    @BeforeEach
    void setUp() {
        mockReview = new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 5, "Box bikin tidak turu");
        mockReview.setReviewId(1L);
    }

    @Test
    void testCreateReview() {
        assertEquals("28ab0b8c-b580-46db-8308-a758e4bac948", mockReview.getBoxId());
        assertEquals(1, mockReview.getUserId());
        assertEquals(5, mockReview.getRating());
        assertEquals("Box bikin tidak turu", mockReview.getReviewText());
        assertNotNull(mockReview.getLastModified());
    }

    @Test
    void testCreateInvalidRating() {
        assertThrows(IllegalArgumentException.class, () -> new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, -1, "negative rating"));
        assertThrows(IllegalArgumentException.class, () -> new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 6, "positive overflow rating"));
    }

    @Test
    void testCreateInvalidReviewText() {
        assertThrows(IllegalArgumentException.class, () -> new Review("28ab0b8c-b580-46db-8308-a758e4bac948", 1, 5, ""));
    }

    @Test
    void testInitialPendingState() {
        assertTrue(mockReview.getStatus() instanceof PendingState);
    }

    @Test
    void testApproveTransition() {
        mockReview.approveReview();
        assertTrue(mockReview.getStatus() instanceof ApprovedState);
    }

    @Test
    void testRejectTransition() {
        mockReview.rejectReview();
        assertTrue(mockReview.getStatus() instanceof RejectedState);
    }

    @Test
    void testEditFromRejectedToPending() {
        mockReview.rejectReview();
        int newRating = 4;
        String newReviewText = "plis tolong eksep";
        mockReview.updateReview(newRating, newReviewText);
        assertTrue(mockReview.getStatus() instanceof PendingState);
        assertEquals(newRating, mockReview.getRating());
        assertEquals(newReviewText, mockReview.getReviewText());
        assertNotNull(mockReview.getLastModified());
    }

    @Test
    void testEditFromAcceptedToPending() {
        mockReview.approveReview();
        int newRating = 4;
        String newReviewText = "plis tolong eksep lagi";
        mockReview.updateReview(newRating, newReviewText);
        assertTrue(mockReview.getStatus() instanceof PendingState);
        assertEquals(newRating, mockReview.getRating());
        assertEquals(newReviewText, mockReview.getReviewText());
        assertNotNull(mockReview.getLastModified());
    }
}
