package com.example.foodmap.service;


import com.example.foodmap.dto.Restaurant.*;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import com.example.foodmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.foodmap.exception.ErrorCode.*;



@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;
    private final UserRepository userRepository;

    public static final int DISTANCE = 3;
    //region 식당등록
    @Transactional
    public Long saveRestaurant(@ModelAttribute RestaurantSaveRequestDto requestDto
            , User user) {
        log.info("여기는 service");
        log.info("식당 등록"+ requestDto.getRestaurantName());

        User foundUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        String imagePath = "";
//        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
//            imagePath = "default.jpg"; //s3에 저장된 default 이미지 보내줌.
//        } else {
            imagePath = storageService.uploadFile(requestDto.getImage());
//        }
        log.info("여기는 location 위 ");
        log.info(imagePath);
        Location location = new Location(requestDto.getAddress(), requestDto.getLatitude(), requestDto.getLongitude());
        Restaurant restaurant = new Restaurant(requestDto, imagePath, foundUser, location);
        log.info("여기는 save 위 디비 저장되나요~ ");

        return restaurantRepository.save(restaurant).getId();
    }

    //내 근처 식당 조회 -> 추가: 위치기반으로 조회해서 가까운 순으로 정렬
    public List<RestaurantResponseDto> getRestaurants(double userLat, double userLon, int page, int size) {

        List<RestaurantResponseDto> restaurants = new ArrayList<>();

        PageRequest pageable = PageRequest.of(page, size);

        List<Restaurant> restaurantList = restaurantRepository.findRestaurantByLocation(userLat, userLon, DISTANCE, pageable);

            for (Restaurant restaurant : restaurantList) {

                double restLat = restaurant.getLocation().getLatitude();
                double restLon = restaurant.getLocation().getLongitude();

                double distance = getDistance(userLat, userLon, restLat, restLon);

                List<Review> reviews = restaurant.getReviews();
//            List<RestaurantLikes> restaurantLikes = restaurant.getRestaurantLikes();

                int spicySum = 0;
                int spicyAvg = 0;
                for (Review review : reviews) {
                    spicySum += review.getSpicy();
                    System.out.println(spicySum);
                }
                spicyAvg = Math.round(spicySum /reviews.size()); //맵기 평균값

                RestaurantResponseDto responseDto = RestaurantResponseDto.builder()
                        .restaurantId(restaurant.getId())
                        .restaurantName(restaurant.getRestaurantName())
                        .location(restaurant.getLocation())
                        .fried(restaurant.getFried())
                        .sundae(restaurant.getSundae())
                        .tteokbokkiType(restaurant.getTteokbokkiType())
                        .spicy(spicyAvg)
                        .restaurantLikesCount(restaurant.getRestaurantLikesCount())
                        .distance(distance)
                        .reviewCount(reviews.size())
                        .image(restaurant.getImage())
                        .build();

                restaurants.add(responseDto);
            }
            return restaurants;
    }
//endregion

    //region 식당 상세 조회
    public RestaurantDetailResponseDto getRestaurantDetail(Long restaurantId, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

////        식당 즐겨찾기 리스트
//        List<RestaurantLikes> restaurantLikesList = restaurant.getRestaurantLikes();
//
//        List<RestaurantLikesDto> restaurantLikesDtoList = new ArrayList<>();
//        if (!restaurantLikesList.isEmpty() || restaurantLikesDtoList.size() != 0) {
//            for (RestaurantLikes restaurantLikes : restaurantLikesList) {
//                RestaurantLikesDto restaurantLikesDto = new RestaurantLikesDto(restaurantLikes.getUser().getId());
//                restaurantLikesDtoList.add(restaurantLikesDto);
//            }
//        }

//        리뷰에서 맵기, 이미지, 해시태그 추출.
        List<Review> reviews = reviewRepository.findByRestaurant(restaurant);
        List<RestaurantReviewResponseDto> restaurantReviewResponseDtos = new ArrayList<>();
        int spicySum = 0;
        int spicyAvg = 0;
        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                spicySum += review.getSpicy();
                System.out.println(spicySum);

                RestaurantReviewResponseDto responseDto = RestaurantReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .image(review.getImage())
                        .restaurantTags(review.getRestaurantTags())
                        .build();

                restaurantReviewResponseDtos.add(responseDto);
            }
            spicyAvg = Math.round(spicySum / restaurantReviewResponseDtos.size()); //맵기 평균값
        }

        //거리
        double userLat = user.getLocation().getLatitude();
        double userLon = user.getLocation().getLongitude();

        double restLat = restaurant.getLocation().getLatitude();
        double restLon = restaurant.getLocation().getLongitude();

        double distance = getDistance(userLat, userLon, restLat, restLon);

        RestaurantDetailResponseDto detailResponseDto = RestaurantDetailResponseDto.builder()
                .restaurantId(restaurantId)
                .restaurantName(restaurant.getRestaurantName())
                .location(restaurant.getLocation())
                .restaurantType(restaurant.getRestaurantType())
                .fried(restaurant.getFried())
                .sundae(restaurant.getSundae())
                .tteokbokkiType(restaurant.getTteokbokkiType())
                .spicy(spicyAvg)
                .restaurantLikesCount(restaurant.getRestaurantLikesCount())
                .restaurantReviews(restaurantReviewResponseDtos)
                .distance(distance)
                .image(restaurant.getImage())
                .build();
        return detailResponseDto;
    }
    //endregion

        /**
         * 두 지점간의 거리 계산
         *
         * @param lat1 지점 1 위도
         * @param lon1 지점 1 경도
         * @param lat2 지점 2 위도
         * @param lon2 지점 2 경도
         *
         */
        private static double getDistance ( double lat1, double lon1, double lat2, double lon2){

            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1609.344; //미터로 계산

            return (dist);
        }

        // This function converts decimal degrees to radians
        private static double deg2rad ( double deg){
            return (deg * Math.PI / 180.0);
        }

        // This function converts radians to decimal degrees
        private static double rad2deg ( double rad){
            return (rad * 180 / Math.PI);
        }


        public List<RankingResponseDto> getTop3ByRestaurant(Restaurant restaurant,User user) {

        ArrayList<RankingResponseDto> myLikeList = new ArrayList<>();
            double userLat = user.getLocation().getLatitude();
            double userLon = user.getLocation().getLongitude();


            List<Restaurant> restaurantLikesList = restaurantRepository.findRestaurantsByRestaurantLikesCountDesc(restaurant);

        for (Restaurant restaurantList : restaurantLikesList) {
            double restLat = restaurantList.getLocation().getLatitude();
            double restLon = restaurantList.getLocation().getLongitude();
            double distance = getDistance(userLat, userLon, restLat, restLon);

            RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
                    .restaurantId(restaurantList.getId())
                    .restaurantName(restaurantList.getRestaurantName())
                    .restaurantLikesCount(restaurantList.getRestaurantLikesCount())
                    .image(restaurantList.getImage())
                    .distance(distance)
                    .build();

            myLikeList.add(rankingResponseDto);

        }



        myLikeList.sort((a, b) -> b.getRestaurantLikesCount() - a.getRestaurantLikesCount());
            myLikeList.removeIf((a) -> a.getDistance() > 3000);
        if (myLikeList.size() > 3 ) {
            return myLikeList.stream()
                    .limit(3)
                    .collect(Collectors.toList());
        }
        return myLikeList;
    }

}