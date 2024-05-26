package id.ac.ui.cs.advprog.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @JsonIgnore
    private ReviewState status;

    @Column(name = "status_string", nullable = false)
    private String statusString;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    //reference: https://stackoverflow.com/questions/30595534/persisting-restoring-current-state-in-spring-statemachine
    @PostLoad
    public void initStatus() {
        switch (statusString) {
            case "PENDING":
                this.status = new PendingState(this);
                break;
            case "APPROVED":
                this.status = new ApprovedState(this);
                break;
            case "REJECTED":
                this.status = new RejectedState(this);
                break;
            default:
                throw new IllegalStateException("Invalid status string: " + statusString);
        }
    }

    public Review (){}

    public Review(String boxId, Integer userId, int rating, String reviewText){
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
        this.lastModified = LocalDateTime.now();
    }

    public void approveReview() {
        this.status.approveReview();
    }

    public void rejectReview() {
        this.status.rejectReview();
    }

    public void updateReview(int newRating, String newReviewText) {
        this.status.updateReview(newRating, newReviewText);
    }
}
