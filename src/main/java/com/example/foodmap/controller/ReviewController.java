package com.example.foodmap.controller;


import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.dto.review.ReviewResponseDto;
import com.example.foodmap.dto.review.ReviewUpdateRequestDto;

import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"리뷰"})
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 작성하기")
    @PostMapping("/restaurants/review/{restaurantId}")
    public ResponseEntity<String> createReview(@PathVariable Long restaurantId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @ModelAttribute ReviewRequestDto reviewRequestDto) {

        reviewService.createReview(restaurantId, reviewRequestDto, userDetails.getUser());

        return ResponseEntity.ok()
                .body("리뷰 작성 완료!");
    }

    @ApiOperation(value = "리뷰 수정하기")
    @PutMapping("/restaurants/review/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @ModelAttribute ReviewUpdateRequestDto reviewUpdateRequestDto) {
        reviewService.updateReview(reviewId, reviewUpdateRequestDto, userDetails.getUser());

        return ResponseEntity.ok()
                .body("리뷰 수정 완료!");
    }

    @ApiOperation(value = "리뷰 삭제하기")
    @DeleteMapping("/restaurants/review/{reviewId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long reviewId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteRestaurant(reviewId, userDetails.getUser());

        return ResponseEntity.ok()
                .body("리뷰 삭제 완료!");
    }

        @ApiOperation(value = "다른 사람이 쓴 리뷰 조회")
        @GetMapping("/review/{userId}")
        public ResponseEntity<List<ReviewResponseDto>> showReview (@PathVariable Long userId){


            List<ReviewResponseDto> requestDto = reviewService.showReview(userId);

            return ResponseEntity.ok()
                    .body(requestDto);

    }
}
