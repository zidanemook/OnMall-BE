package shop.mookmall.com.core.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import shop.mookmall.com.core.util.MyRatingUtils;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.product.ProductRepository;
import shop.mookmall.com.model.product.ProductType;
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
                            , 0f
                            , ProductType.PRODUCT_TYPE_FOOD
                            , "https://cdn.pixabay.com/photo/2016/12/26/17/28/spaghetti-1932466_1280.jpg"));

            productRepository.save(
                    newProduct("나침반"
                            , 20700
                            , "Nachdem"
                            , 0f
                            , ProductType.PRODUCT_TYPE_LIFE
                            , "https://cdn.pixabay.com/photo/2023/10/18/15/43/compass-8324516_1280.jpg"));

            productRepository.save(
                    newProduct("고대 미술"
                            , 41700
                            , "LoveBooks"
                            , 0f
                            , ProductType.PRODUCT_TYPE_BOOK
                            , "https://cdn.pixabay.com/photo/2022/12/01/00/13/antique-7627999_1280.jpg"));

            productRepository.save(
                    newProduct("편안 침대"
                            , 401700
                            , "Base"
                            , 0f
                            , ProductType.PRODUCT_TYPE_FURNITURE
                            , "https://cdn.pixabay.com/photo/2021/11/08/00/30/bedroom-6778193_640.jpg"));

            for (int i = 0; i < 30; i++) {
                productRepository.save(
                        newProduct("최신 사과폰"
                                , 1700000
                                , "사과"
                                , 0f
                                , ProductType.PRODUCT_TYPE_ELECTRONICS
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
