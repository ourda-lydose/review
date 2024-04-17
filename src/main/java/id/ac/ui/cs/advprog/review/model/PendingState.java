package id.ac.ui.cs.advprog.review.model;

public class PendingState implements ReviewState {
    private final Review review;

    public PendingState(Review review) {
        this.review = review;
    }

    @Override
    public void editReview(int newRating, String newReviewText) {
        
    }

    @Override
    public void approveReview() {
        review.setStatus(new ApprovedState(review));
    }

    @Override
    public void rejectReview() {
        review.setStatus(new RejectedState(review));
    }

    @Override
    public void deleteReview() {
    }
}