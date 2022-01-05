package com.example.foodmap.service;

import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.dto.review.ReviewResponseDto;
import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.Review;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;

    //region 리뷰 작성
    @Transactional
    public void createReview(Long restaurantId, ReviewRequestDto reviewRequestDto, User user) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NullPointerException("해당 음식점이 존재하지 않습니다.")
        );

//        String imagePath = storageService.uploadFile(image);

        Review review = new Review(reviewRequestDto, user, restaurant);
        reviewRepository.save(review);

    }
    //endregion

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new NullPointerException("해당 리뷰가 존재하지 않습니다.")
        );
        Restaurant restaurant = restaurantRepository.findById(reviewId).orElseThrow(
                () -> new NullPointerException("해당 음식점이 존재하지 않습니다.")
        );

//        String imagePath = storageService.uploadFile(image);

        review.update(reviewUpdateRequestDto,user,restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long reviewId, User user) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (!review.isPresent()) {
            throw new NullPointerException("존재하지 않는 댓글입니다.");
        }
        if(!user.getId().equals(review.get().getUser().getId())){
            throw new IllegalArgumentException(("댓글의 작성자만 삭제가 가능합니다."));
        }
        reviewRepository.deleteById(reviewId);
    }

    //region 리뷰 조회
    public List<ReviewResponseDto> showReview(Long userId) {


        List<ReviewResponseDto> reviewLists = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAllByUserId(userId);
        for (Review review : reviewList) {
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .userId(review.getUser().getId())
                    .restaurantId(review.getRestaurant().getId())
                    .image(review.getImage())
                    .build();
            reviewLists.add(reviewResponseDto);
        }
        return reviewLists;
    }

   // endregion
}