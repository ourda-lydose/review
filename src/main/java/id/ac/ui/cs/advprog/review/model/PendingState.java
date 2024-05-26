package id.ac.ui.cs.advprog.review.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(value = {"review"})
public class PendingState implements ReviewState {
    private final Review review;

    public PendingState(Review review) {
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
        review.setStatus(new ApprovedState(review));
        review.setStatusString(StatusEnum.APPROVED.toString());

    }

    @Override
    public void rejectReview() {
        review.setStatus(new RejectedState(review));
        review.setStatusString(StatusEnum.REJECTED.toString());

    }
}