package shop.mookmall.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.mookmall.com.core.auth.session.MyUserDetails;
import shop.mookmall.com.dto.ResponseDTO;
import shop.mookmall.com.dto.rating.RatingRequest;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.rating.Rating;
import shop.mookmall.com.model.user.User;
import shop.mookmall.com.service.ProductService;
import shop.mookmall.com.service.RatingService;
import shop.mookmall.com.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class RatingController {
    private final RatingService ratingService;
    private final ProductService productService;

    private final UserService userService;

    @PostMapping("/auth/rating")
    public ResponseEntity<?> rateStar(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                      @RequestBody RatingRequest.RatingDTO ratingDTO){

        int score = ratingDTO.getScore();
        Long productId = ratingDTO.getProductId();

        User user = userService.findById(myUserDetails.getUser().getId());
        Product productPS = productService.findProductById(productId);

        //평점 테이블 추가
        ratingService.addRating(productPS, user, score);

        //Product 평균평점 갱신
        float avgRate = ratingService.getAvgRatingByProductId(productId);
        productPS.setRating(avgRate);
        productService.saveProduct(productPS);

        //평균평점 반환
        ResponseDTO<?> responseDTO = new ResponseDTO<>(avgRate);
        return ResponseEntity.ok(responseDTO);
    }

    //유저가 이 상품에 평점을 준경우 반환
    @GetMapping("/auth/currentrating")
    public ResponseEntity<?> getCurrentRating(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                              @RequestParam(required = true) Long productId){

        Rating rating = ratingService.getRating(productService.findProductById(productId), myUserDetails.getUser());

        ResponseDTO<?> responseDTO = new ResponseDTO<>(rating.getScore());
        return ResponseEntity.ok(responseDTO);
    }
}
