package org.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String title; // 게시글 제목
    private String body; // 게시글 내용
    private UserDTO writer; // 작성자 정보
}

