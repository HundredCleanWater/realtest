package com.example.foodmap.model;


import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Review extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int spicy;

    @Column(nullable = false)
    private String restaurantTags;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ColumnDefault(value="0")
    private int reviewLike ;

    @Builder
    public Review(ReviewRequestDto reviewRequestDto, User user,Restaurant restaurant) {
        this.user =user;
        this.restaurant = restaurant;
        this.content = reviewRequestDto.getContent();
        this.spicy=reviewRequestDto.getSpicy();
        this.restaurantTags = reviewRequestDto.getRestaurantTags();
        this.image = "default.png";
    }

    public void update(ReviewUpdateRequestDto reviewUpdateRequestDto,User user,Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
        this.content = reviewUpdateRequestDto.getContent();
        this.spicy=reviewUpdateRequestDto.getSpicy();
        this.restaurantTags=reviewUpdateRequestDto.getRestaurantTags();
        this.image="default.png";
    }
}