package com.example.foodmap.controller;

import com.example.foodmap.dto.user.*;
import com.example.foodmap.model.Location;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.KakaoUserService;
import com.example.foodmap.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final KakaoUserService kakaoUserService;
    private final UserService userService;


    @GetMapping("/user/kakao/callback")
    public ResponseEntity<KakaoUserResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        KakaoUserResponseDto kakaoUserResponseDto = kakaoUserService.kakaoLogin(code,response);
        return ResponseEntity.ok().body(kakaoUserResponseDto);
    }
    //닉네임변경&중복확인
//    @PutMapping("/user/userCheck")
//    public ResponseEntity<UsernameCheckResponseDto> changeUser(@RequestBody UsernameCreateDto usernameCreateDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        log.info(userDetails.getUsername());
//      UsernameCheckResponseDto usernameCheckResponseDto =  userService.changeUser(usernameCreateDto,userDetails);
//
//        return ResponseEntity.ok().body(usernameCheckResponseDto);
//    }

    //유저위치 저장(주소, 위도, 경도)
    @ApiOperation("유저위치 등록")
    @PutMapping("/user/location")
    public Location saveLocation(@RequestBody UserLocationDto locationDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.saveUserLocation(locationDto, userDetails.getUser());
    }

    //유저정보
    @GetMapping("/userInfo")
    public KakaoInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.userInfo(userDetails);
    }

//    사용자프로필수정(프로필사진, 닉네임, 주소,위도,경도)
    @PutMapping("/userInfo/update")
    public ResponseEntity<?> updateUserInfo(
            @ModelAttribute UserInfoUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        userService.updateUserInfo(requestDto, userDetails);
        return ResponseEntity.ok().body("등록되었습니다.");
    }



}
