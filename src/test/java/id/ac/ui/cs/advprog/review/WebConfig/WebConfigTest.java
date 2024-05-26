package id.ac.ui.cs.advprog.review.WebConfig;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.Mockito.*;

public class WebConfigTest {

    @Test
    public void testCorsConfigurer() {
        CorsRegistry corsRegistry = mock(CorsRegistry.class);

        WebMvcConfigurer webMvcConfigurer = new WebConfig().corsConfigurer();

        webMvcConfigurer.addCorsMappings(corsRegistry);

        verify(corsRegistry).addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
