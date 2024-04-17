package id.ac.ui.cs.advprog.review.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private String reviewId;
    private String boxId;
    private int userId;
    private int rating;
    private String reviewText;
    private ReviewState status;

    public Review(String reviewId, String boxId, int userId, int rating, String reviewText){
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new IllegalArgumentException("Review text can't be empty");
        }

        this.reviewId = reviewId;
        this.boxId = boxId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.status = new PendingState(this);
    }

    public void approveReview() {
        this.status.approveReview();
    }

    public void rejectReview() {
        this.status.rejectReview();
    }

    public void editReview(int newRating, String newReviewText) {
        this.status.editReview(newRating, newReviewText);
    }
}
