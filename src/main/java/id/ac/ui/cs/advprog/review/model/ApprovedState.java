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
    public void deleteReview() {

    }

    @Override
    public void approveReview() {
    }

    @Override
    public void rejectReview() {
        throw new IllegalStateException("No, you cant do that :((");
    }
}
