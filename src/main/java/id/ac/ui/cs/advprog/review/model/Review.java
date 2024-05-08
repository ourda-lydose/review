package id.ac.ui.cs.advprog.review.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static id.ac.ui.cs.advprog.review.model.StatusEnum.*;

@Getter
@Setter
@Table(name = "reviews")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "box_id", nullable = false)
    private String boxId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @Transient
    private ReviewState status;

//     for DB
//    @Enumerated(EnumType.STRING)
    @Column(name = "status_string", nullable = false)
    private String statusString;

    public Review(Long reviewId, String boxId, Integer userId, int rating, String reviewText){
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new IllegalArgumentException("Review text can't be empty");
        }

        this.boxId = boxId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.status = new PendingState(this);
        this.statusString = PENDING.toString();
    }

    public Review() {

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
