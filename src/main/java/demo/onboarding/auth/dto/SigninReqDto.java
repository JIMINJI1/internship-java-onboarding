package demo.onboarding.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SigninReqDto {
    @NotBlank(message = "닉네임을 입력해 주세요")
    private String username;
    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password;
}
