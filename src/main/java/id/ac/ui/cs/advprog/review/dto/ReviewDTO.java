package id.ac.ui.cs.advprog.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private String boxId;
    private Integer userId;
    private int rating;
    private String reviewText;
}
