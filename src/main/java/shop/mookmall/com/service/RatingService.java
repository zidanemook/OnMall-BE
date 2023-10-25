package shop.mookmall.com.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.mookmall.com.core.exception.Exception400;
import shop.mookmall.com.core.util.MyRatingUtils;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.rating.Rating;
import shop.mookmall.com.model.rating.RatingRepository;
import shop.mookmall.com.model.user.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> getAllRatingsByProductId(Long productId) {
        return ratingRepository.findByProductId(productId);
    }

    public float getAvgRatingByProductId(Long productId){
        List<Rating> ratingList = getAllRatingsByProductId(productId);

        return MyRatingUtils.getAvgRating(ratingList);
    }

    public Rating addRating(Product product, User user, int score){
        // 이미 해당 product와 user에 대한 평가가 있는지 확인
        Optional<Rating> existingRating = ratingRepository.findByProductAndUser(product, user);

        if (existingRating.isPresent()) {
            // 이미 평가가 있으면 값을 갱신
            Rating rating = existingRating.get();
            rating.setScore(score);

            return ratingRepository.save(rating);
        }

        // 새로운 평가 추가
        return ratingRepository.save(Rating.builder()
                .score(score)
                .product(product)
                .user(user)
                .build());
    }

    public Optional<Rating> getRating(Product product, User user){
        return ratingRepository.findByProductAndUser(product, user);
    }

}
