package com.example.foodmap.repository;

import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    Optional<Restaurant> findById(Long restaurantId);

    List<Restaurant> findAllByUser(User user);

    String HAVERSINE_PART = "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.location.latitude)) * cos(radians(r.location.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.location.latitude))))";

    @Query("SELECT r FROM Restaurant r  WHERE "+HAVERSINE_PART+" < :distance ORDER BY "+HAVERSINE_PART+" ASC")
    List<Restaurant> findRestaurantByLocation(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance,
            Pageable pageable
    );

    @Modifying
    @Query(value = "update Restaurant r set r.restaurantLikesCount= r.restaurantLikesCount + 1 where r.id = :id")
    void upLikeCnt(Long id);

    @Modifying
    @Query(value = "update Restaurant r set r.restaurantLikesCount= r.restaurantLikesCount - 1 where r.id = :id")
    void downLikeCnt(Long id);

    @Query("select r from Restaurant r  order by r.restaurantLikesCount desc ")
    List<Restaurant> findRestaurantsByRestaurantLikesCountDesc(Restaurant restaurant);

}
