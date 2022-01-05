package com.example.foodmap.dto.meeting;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCommentResponseDto {

    private Long commentId;
    private Long userId;
    private String nickname;
    private String content;
    private List<MeetingCommentResponseDto> children = new ArrayList<>();

    @Builder
    public MeetingCommentResponseDto(Long id, String content, Long userId, String nickname) {
        this.commentId = id;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
    }

}
