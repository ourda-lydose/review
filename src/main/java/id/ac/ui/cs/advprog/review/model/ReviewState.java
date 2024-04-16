package id.ac.ui.cs.advprog.review.model;
public interface ReviewState {
    void EditReview();
    void DeleteReview();
    void approveReview();
    void rejectReview();
}