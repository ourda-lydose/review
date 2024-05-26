package id.ac.ui.cs.advprog.review;

import id.ac.ui.cs.advprog.review.controller.ReviewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewApplicationTests {

    @Autowired
    private ReviewController reviewController;

    @Test
    void contextLoads() {
        ReviewApplication.main(new String[]{});
        assertThat(reviewController).isNotNull();
    }

}
