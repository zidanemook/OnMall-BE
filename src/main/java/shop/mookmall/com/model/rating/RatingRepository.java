package shop.mookmall.com.model.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.user.User;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProductId(Long productId);

    Optional<Rating> findByProductAndUser(Product product, User user);

}
