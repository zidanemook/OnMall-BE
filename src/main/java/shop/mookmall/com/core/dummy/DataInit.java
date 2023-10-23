package shop.mookmall.com.core.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import shop.mookmall.com.core.util.MyRatingUtils;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.product.ProductRepository;
import shop.mookmall.com.model.rating.Rating;
import shop.mookmall.com.model.rating.RatingRepository;
import shop.mookmall.com.model.user.User;
import shop.mookmall.com.model.user.UserRepository;
import shop.mookmall.com.model.user.UserRole;

import java.util.List;
import java.util.Optional;

@Component
public class DataInit extends DummyEntity{
    @Profile("dev")
    @Bean
    CommandLineRunner initUser(UserRepository userRepository){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return args -> {
            userRepository.save(newUser("김씨", "kim@nate.com"));

            userRepository.save(newUser("고씨", "go@nate.com"));

            User userPS = userRepository.save(newUser("요다", "yoda@nate.com"));
            userPS.setRole(UserRole.ROLE_SELLER);
            userRepository.save(userPS);
        };
    }

    @Profile("dev")
    @Bean
    CommandLineRunner initProduct(ProductRepository productRepository){

        return args -> {
            productRepository.save(
                    newProduct("신선 토마토"
                            , 11700
                            , "신선농장"
                            , 9.0f
                            , "https://cdn.pixabay.com/photo/2016/12/26/17/28/spaghetti-1932466_1280.jpg"));

            for (int i = 0; i < 30; i++) {
                productRepository.save(
                        newProduct("최신 사과폰"
                                , 1700000
                                , "사과"
                                , 8.1f
                                , "https://cdn.pixabay.com/photo/2014/08/05/10/27/iphone-410311_960_720.jpg"));
            }


        };
    }

    @Profile("dev")
    @Bean
    CommandLineRunner initRating(RatingRepository ratingRepository, ProductRepository productRepository, UserRepository userRepository){
        return args -> {

            ratingRepository.save(
                    newRating(5, productRepository.findById(1L).get(), userRepository.findById(1L).get()));

            ratingRepository.save(
                    newRating(10, productRepository.findById(1L).get(), userRepository.findById(2L).get()));

            List<Rating> ratingList = ratingRepository.findByProductId(1L);
            float avg = MyRatingUtils.getAvgRating(ratingList);

            Product product = productRepository.findById(1L).get();
            product.setRating(avg);
            productRepository.save(product);
        };
    }
}
