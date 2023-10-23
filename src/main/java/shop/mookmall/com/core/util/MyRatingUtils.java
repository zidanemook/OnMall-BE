package shop.mookmall.com.core.util;

import shop.mookmall.com.model.rating.Rating;

import java.util.List;

public class MyRatingUtils {

    public static float getAvgRating(List<Rating> ratingList){

        float total = 0f;
        for(Rating rating : ratingList){
            total += rating.getScore();
        }
        return total/ratingList.size();
    }
}
