package com.example.foodmap.service;

import com.example.foodmap.dto.meeting.*;
import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingComment;
import com.example.foodmap.model.MeetingParticipate;
import com.example.foodmap.repository.MeetingCommentRepository;
import com.example.foodmap.repository.MeetingParticipateRepository;
import com.example.foodmap.repository.MeetingRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingParticipateRepository meetingParticipateRepository;
    private final MeetingCommentRepository meetingCommentRepository;

    //모임등록글
    @Transactional
    public void creatMeeting(MeetingCreatRequestDto meetingCreatRequestDto, UserDetailsImpl userDetails) {
            Long kakaoId = userDetails.getUser().getKakaoId();
            userRepository.findByKakaoId(kakaoId).orElseThrow(
                    ()-> new NullPointerException("로그인이 필요합니다.")
            );
            Meeting meeting = new Meeting(userDetails.getUser(), meetingCreatRequestDto);

            meetingRepository.save(meeting);

            //모임등록 한사람 자동참가인원+1 자기자신
            MeetingParticipate meetingParticipate = new MeetingParticipate(meeting, userDetails);
            meetingParticipateRepository.save(meetingParticipate);

    }

    //상세모임 게시글
    @Transactional
    public MeetingDetailResponseDto getMeeting(Long meetingId, UserDetailsImpl userDetails) {
        Long kakaoId = userDetails.getUser().getKakaoId();
        userRepository.findByKakaoId(kakaoId).orElseThrow(
                ()-> new NullPointerException("로그인이 필요합니다.")
        );

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                ()->new NullPointerException("존재하지 않는 게시물입니다.")
        );
        //조회수 증가
        viewCountUp(meeting.getId());
        //댓글 정보


        MeetingInfoResponseDto meetingInfoResponseDto = new MeetingInfoResponseDto(
                meeting.getMeetingTitle(),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getMeetingDate(),
                meeting.getLocation(),
                meeting.getLimitPeople(),
                meeting.getNowPeople(),
                meeting.getContent(),
                meeting.getRestaurant(),
                meeting.getViewCount(),
                meeting.getModifiedAt(),
                meeting.getUser().getId(),
                meeting.getId()
        );
        //반환할 객체 생성

        return new MeetingDetailResponseDto(meetingInfoResponseDto,commentAll(meetingId,userDetails));
    }


    //댓글 조회
    public List<MeetingCommentResponseDto> commentAll(Long meetingId,UserDetailsImpl userDetails) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return convertNestedStructure(meetingCommentRepository.findMeetingCommentByMeeting(meeting));
    }
    public MeetingCommentResponseDto convertCommentToDto(MeetingComment comment){ //댓글삭제
        return new MeetingCommentResponseDto(comment.getId(),comment.getContent(),comment.getUser().getId(), comment.getUser().getNickname());
    }


    //댓글 구조만들기기
    private List<MeetingCommentResponseDto> convertNestedStructure(List<MeetingComment> comments) { //계층형 구조 만들기
        List<MeetingCommentResponseDto> result = new ArrayList<>();
        Map<Long, MeetingCommentResponseDto> map = new HashMap<>();
        comments.forEach(c -> {
            MeetingCommentResponseDto dto = convertCommentToDto(c);
            map.put(dto.getCommentId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);//양방향 연관관계를 사용해서 자식 코멘트에 댓글 등록
            else result.add(dto);
        });
        return result;
    }



    // 조회수
    public void viewCountUp(Long meetingId) {
        meetingRepository.updateView(meetingId);
    }


//    //본인만 모임글 수정
//    @Transactional
//    public void updateMeeting(Long meetingId,MeetingCreatRequestDto meetingCreatRequestDto, UserDetailsImpl userDetails) {
//        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
//                ()->new NullPointerException("존재하지 않는 게시물입니다.")
//        );
//        if(meeting.getUser().getId().equals(userDetails.getUser().getId())) {
//
//            meeting.update(meetingCreatRequestDto);
//
//        }else {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }
//    }

    //모임글 삭제
    @Transactional
    public void deleteMeeting(Long meetingId, UserDetailsImpl userDetails) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                ()->new NullPointerException("존재하지 않는 게시물입니다.")
        );
        if(meeting.getUser().getUsername().equals(userDetails.getUsername())){
            meetingRepository.deleteById(meetingId);
        }else {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
    }

    //모임전체 조회리스트
    @Transactional
    public List<MeetingTotalListResponseDto> getMeetingLsit() {
        //반환할 리스트
        List<MeetingTotalListResponseDto> meetingTotalListResponseDtoList = new ArrayList<>();


        //반환 목록에 들어갈 데이터 찾을 리스트
        List<Meeting> meetingList = meetingRepository.findAllByOrderByModifiedAtDesc();

        for(Meeting meeting:meetingList){
            MeetingTotalListResponseDto meetingTotalDto = new MeetingTotalListResponseDto(
                    meeting.getMeetingTitle(),
                    meeting.getStartDate(),
                    meeting.getEndDate(),
                    meeting.getMeetingDate(),
                    meeting.getLocation(),
                    meeting.getLimitPeople(),
                    meeting.getNowPeople(),
                    meeting.getContent(),
                    meeting.getRestaurant(),
                    meeting.getViewCount(),
                    meeting.getModifiedAt(),
                    meeting.getUser().getId(),
                    meeting.getId()
            );
            meetingTotalListResponseDtoList.add(meetingTotalDto);

        }
        return meetingTotalListResponseDtoList;
    }
}
