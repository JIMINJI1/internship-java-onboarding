package demo.onboarding.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SignupResDto {
    private String username;
    private String nickname;
    private List<AuthorityDto> authorities;
}
