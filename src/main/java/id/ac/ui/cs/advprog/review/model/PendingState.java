package id.ac.ui.cs.advprog.review.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"review"})
public class PendingState implements ReviewState {
    private final Review review;

    public PendingState(Review review) {
        this.review = review;
    }

    @Override
    public void editReview(int newRating, String newReviewText) {
        review.setRating(newRating);
        review.setReviewText(newReviewText);
        review.setStatus(new PendingState(review));
    }

    @Override
    public void approveReview() {
        review.setStatus(new ApprovedState(review));
    }

    @Override
    public void rejectReview() {
        review.setStatus(new RejectedState(review));
    }
}