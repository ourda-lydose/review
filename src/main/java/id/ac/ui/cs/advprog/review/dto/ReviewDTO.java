package id.ac.ui.cs.advprog.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private String boxId;
    private Integer userId;
    private int rating;
    private String reviewText;
    private String statusString;
}
