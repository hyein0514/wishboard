package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;
    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    
    public static UserDTO toDto(User member) {
        return new UserDTO(
                member.getNickname(),
                member.getUserId(),
                member.getPassword()
        );
    }
}