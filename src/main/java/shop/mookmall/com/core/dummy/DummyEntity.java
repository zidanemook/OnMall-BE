package shop.mookmall.com.core.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.product.ProductType;
import shop.mookmall.com.model.rating.Rating;
import shop.mookmall.com.model.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DummyEntity {

    public User newUser(String username, String email){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(email)
                .role("ROLE_CUSTOMER")
                .build();
    }

    public Product newProduct(String productname, int price, String manufacturer, float rating, ProductType productType, String photo){

        return Product.builder()
                .productName(productname)
                .price(price)
                .manufacturer(manufacturer)
                .rating(rating)
                .photo(photo)
                .productType(productType)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Rating newRating(int score, Product product, User user){
        return Rating.builder()
                .score(score)
                .product(product)
                .user(user)
                .build();
    }
}
