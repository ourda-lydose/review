package id.ac.ui.cs.advprog.review.model;

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
    }

    @Override
    public void approveReview() {
    }

    @Override
    public void rejectReview() {
        throw new IllegalStateException("Can't reject approved review");
    }
}
