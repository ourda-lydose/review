package id.ac.ui.cs.advprog.review.model;

import java.time.LocalDateTime;

public class ApprovedState implements ReviewState {
    private final Review review;

    public ApprovedState(Review review) {
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
        //approving approved state
    }

    @Override
    public void rejectReview() {
        throw new IllegalStateException("Can't reject approved review");
    }
}
