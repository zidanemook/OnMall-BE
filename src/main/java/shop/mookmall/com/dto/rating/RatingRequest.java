package shop.mookmall.com.dto.rating;

import lombok.Getter;
import lombok.Setter;

public class RatingRequest {

    @Getter
    @Setter
    public static class RatingDTO {
        private int score;
        private Long productId;
    }
}
