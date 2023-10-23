package shop.mookmall.com.model.rating;

import lombok.*;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rating_tb")
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Score must be equal or greater than 1")
    @Max(value = 10, message = "Score must be equal or less than 10")
    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Rating(Long id, int score, Product product, User user){
        this.id = id;
        this.score = score;
        this.product = product;
        this.user = user;
    }
}
