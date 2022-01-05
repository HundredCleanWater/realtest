package com.example.foodmap.dto.Restaurant;

import com.example.foodmap.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RestaurantDetailResponseDto {
    //식당 상세페이지 responseDto
    private Long restaurantId;
    private String restaurantName;
    private Location location;
    private double distance; //사용자와의 거리
    private String restaurantType;
    private String fried; //튀김판매유무
    private String sundae; //순대판매형태
    private String tteokbokkiType; //밀떡,살떡
    private String image;
    private int spicy; //맵기 정도
    private int restaurantLikesCount; //즐겨찾기
    private List<RestaurantReviewResponseDto> restaurantReviews;
}