package com.example.foodmap.service;

import com.example.foodmap.dto.meeting.MeetingCommentCreateRequestDto;
import com.example.foodmap.dto.meeting.MeetingUpdateRequestDto;
import com.example.foodmap.model.MeetingComment;
import com.example.foodmap.repository.MeetingCommentRepository;
import com.example.foodmap.repository.MeetingRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MeetingCommentService {

    private final MeetingRepository meetingRepository;
    private final MeetingCommentRepository meetingCommentRepository;
    private final UserRepository userRepository;

    //모임 댓글 등록
    @Transactional
    public  MeetingComment createComment(MeetingCommentCreateRequestDto meetingCommentCreateRequestDto,Long meetingId, UserDetailsImpl userDetails) {
          return meetingCommentRepository.save(
                  new MeetingComment(meetingCommentCreateRequestDto.getContent(),
                      userRepository.findByKakaoId(userDetails.getUser().getKakaoId()).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저 정보입니다.")),
                      meetingRepository.findById(meetingId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글 입니다.")),
                          Optional.ofNullable(meetingCommentCreateRequestDto.getParentId())
                                  .map(id -> meetingCommentRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                                  .orElse(null)));


    }

    //모임댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId, UserDetailsImpl userDetails) {
        MeetingComment comment = meetingCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(userDetails.getUser().getId())) throw new IllegalArgumentException("본인 댓글만 삭제 할 수있습니다.");
        meetingCommentRepository.delete(comment);

        return commentId;
    }

    //모임 댓글 수정
    @Transactional
    public void updateComment(MeetingUpdateRequestDto meetingUpdateRequestDto, Long commentId, UserDetailsImpl userDetails) {

        MeetingComment meetingComment = meetingCommentRepository.findById(commentId).orElseThrow(
                ()->new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        if(meetingComment.getUser().getUsername().equals(userDetails.getUsername())) {
            meetingComment.updateMeeting(meetingUpdateRequestDto);
        }else {
            throw new IllegalArgumentException("본인 댓글만 수정 할 수 있습니다.");
        }

    }

}
