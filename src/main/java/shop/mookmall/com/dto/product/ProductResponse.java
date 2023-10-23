package shop.mookmall.com.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductResponse {

    @Getter
    @Setter
    public static class ProductDTO{

        private Long id;
        private String productname;
        private int price;
        private String manufacturer;
        private float rating;
        private String photo;
        private String createdAt;

        public ProductDTO(Long id, String productname, int price, String manufacturer, float rating, String photo, String createdAt){
            this.id = id;
            this.productname = productname;
            this.price = price;
            this.manufacturer = manufacturer;
            this.rating = rating;
            this.photo = photo;
            this.createdAt = createdAt;
        }
    }
}
