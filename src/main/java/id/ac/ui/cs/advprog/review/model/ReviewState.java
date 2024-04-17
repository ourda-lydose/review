package id.ac.ui.cs.advprog.review.model;
public interface ReviewState {
    void editReview(int newRating, String newReviewText);
    void deleteReview();
    void approveReview();
    void rejectReview();
}