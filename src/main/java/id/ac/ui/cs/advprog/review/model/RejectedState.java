package id.ac.ui.cs.advprog.review.model;

import java.time.LocalDateTime;

public class RejectedState implements ReviewState {
    private final Review review;

    public RejectedState(Review review) {
        this.review = review;
    }

    @Override
    public void updateReview(int newRating, String newReviewText) {
        review.setRating(newRating);
        review.setReviewText(newReviewText);
        review.setStatus(new PendingState(review));
        review.setStatusString(StatusEnum.PENDING.toString());
        review.setLastModified(LocalDateTime.now());
    }

    @Override
    public void approveReview() {
        throw new IllegalStateException("Bro must be kidding for approving a rejected review...");
    }

    @Override
    public void rejectReview() {
        // rejecting rejected state
    }
}
