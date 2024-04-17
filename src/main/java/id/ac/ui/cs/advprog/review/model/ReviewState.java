package id.ac.ui.cs.advprog.review.model;
public interface ReviewState {
    void editReview();
    void deleteReview();
    void approveReview();
    void rejectReview(int newRating, String newReviewText);
}