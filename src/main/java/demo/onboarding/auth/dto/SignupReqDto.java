package demo.onboarding.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class SignupReqDto {
    @NotBlank(message = "이름을 입력해 주세요")
    private String username;
    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password;
    @NotBlank(message = "닉네임을 입력해 주세요")
    private String nickname;
}
