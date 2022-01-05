package com.example.foodmap.service;

import com.example.foodmap.dto.user.KakaoInfoResponseDto;
import com.example.foodmap.dto.user.UserInfoUpdateRequestDto;
import com.example.foodmap.dto.user.UserLocationDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Location;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.example.foodmap.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    //region 유저 위치 저장
    @Transactional
    public Location saveUserLocation(UserLocationDto locationDto, User user) {
        User foundUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        Location location = new Location(locationDto);
        foundUser.saveLocation(location);

        return foundUser.getLocation();
    }

    public KakaoInfoResponseDto userInfo(UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        return new KakaoInfoResponseDto(
                user.getNickname(),
                user.getProfileImage(),
                user.getKakaoId(),
                user.getLevel(),
                user.getLocation()
        );
    }

    @Transactional
    public void updateUserInfo(UserInfoUpdateRequestDto requestDto, UserDetailsImpl userDetails) {

        if (userDetails != null) {
            User foundUser = userRepository.findByKakaoId(userDetails.getUser().getKakaoId()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );
            MultipartFile file = requestDto.getProfileImage();

            String profileImagePath = "hello";
            if (file != null) {
                profileImagePath = storageService.uploadFile(file);
            } else {
                profileImagePath = "default.png";
            }

            String nickname = requestDto.getNickname();
            if (nickname != null) {
                //닉네임 중복검사
                if (userRepository.existsByNickname(nickname)) {
                    throw new CustomException(DUPLICATE_RESOURCE);
                }
                //닉네임은 10자 이내로 제한
                if (nickname.length() > 10) {
                    throw new CustomException(WRONG_NICKNAME_LENGTH);
                }

            } else {
                throw new CustomException(NICKNAME_EMPTY);
            }

            Location location = Location.builder()
                    .address(requestDto.getAddress())
                    .latitude(requestDto.getLatitude())
                    .longitude(requestDto.getLongitude()).build();

//            foundUser.saveLocation(location);
            foundUser.updateUserInfo(profileImagePath, nickname, location);

        } else {
            throw new CustomException(UNAUTHORIZED_MEMBER);
        }
    }
}