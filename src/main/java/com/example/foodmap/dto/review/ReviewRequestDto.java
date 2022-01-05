package com.example.foodmap.dto.review;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {
    private String content;
    private int spicy;
    private String restaurantTags;
    private MultipartFile image;

}
