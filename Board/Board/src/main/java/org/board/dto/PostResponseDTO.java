package org.board.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostResponseDTO {
    private String title; // 게시글 제목
    private String body; // 게시글 내용
    private UserDTO writer; // 작성자 정보
    private List<CommentDTO> comments; // 댓글 목록
    private int commentsCount; // 댓글 개수
}
