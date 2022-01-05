package com.example.foodmap.repository;

import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.Review;
import com.example.foodmap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUser(User user);
    List<Review> findAllByUserId(Long userId);

    @Modifying
    @Query(value = "update Review a set a.reviewLike= a.reviewLike + 1 where a.id = :id")
    void upLikeCnt(Long id);

    @Modifying
    @Query(value = "update Review a set a.reviewLike = a.reviewLike - 1 where a.id = :id")
    void downLikeCnt(Long id);

    List<Review> findByRestaurant(Restaurant restaurant);

}
