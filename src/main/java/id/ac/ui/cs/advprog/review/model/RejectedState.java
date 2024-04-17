package id.ac.ui.cs.advprog.review.model;

public class RejectedState implements ReviewState {
    private final Review review;

    public RejectedState(Review review) {
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
        throw new IllegalStateException("Bro must be kidding for approving a rejected review...");
    }

    @Override
    public void rejectReview() {

    }
}
