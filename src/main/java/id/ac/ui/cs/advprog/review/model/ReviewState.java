package id.ac.ui.cs.advprog.review.model;
public interface ReviewState {
    void updateReview(int newRating, String newReviewText);
    void approveReview();
    void rejectReview();
}