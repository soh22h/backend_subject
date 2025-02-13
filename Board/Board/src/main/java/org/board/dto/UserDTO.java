package org.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name; // 사용자 이름
    private String email; // 사용자 이메일 (선택적)
}
