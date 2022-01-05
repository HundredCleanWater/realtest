package com.example.foodmap.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserInfoUpdateRequestDto {
    private MultipartFile profileImage;
    private String nickname;
    private double latitude;
    private double longitude;
    private String address;

    @Builder
    public UserInfoUpdateRequestDto(MultipartFile profileImage, String nickname, double latitude, double longitude, String address) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

}
