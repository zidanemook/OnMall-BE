package shop.mookmall.com.model.product;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_tb")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String productname;

    @Min(value = 1, message = "Price must be equal or greater than 1")
    @Max(value = 2000000000, message = "Price must be equal or less than 2,000,000,000")
    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 128)
    private String manufacturer;

    @Min(0)
    @Max(10)
    private float rating;

    @Column(nullable = false, length = 255)
    private String photo;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
