package com.example.foodmap.dto.Restaurant;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RestaurantReviewResponseDto {
    private Long reviewId;
    private String image;
    private String restaurantTags;

    @Builder
    public RestaurantReviewResponseDto(Long reviewId, String image, String restaurantTags) {
        this.reviewId = reviewId;
        this.image = image;
        this.restaurantTags = restaurantTags;
    }
}