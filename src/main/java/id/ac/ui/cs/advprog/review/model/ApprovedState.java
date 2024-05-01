package id.ac.ui.cs.advprog.review.model;

public class ApprovedState implements ReviewState {
    private final Review review;

    public ApprovedState(Review review) {
        this.review = review;
    }

    @Override
    public void editReview(int newRating, String newReviewText) {
        review.setStatus(new PendingState(review));
        review.setRating(newRating);
        review.setReviewText(newReviewText);
    }

    @Override
    public void approveReview() {
        review.setStatus(new ApprovedState(review));
    }

    @Override
    public void rejectReview() {
        throw new IllegalStateException("Can't reject approved review");
    }
}
