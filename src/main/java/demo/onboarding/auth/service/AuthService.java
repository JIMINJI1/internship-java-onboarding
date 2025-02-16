package demo.onboarding.auth.service;

import demo.onboarding.auth.dto.AuthorityDto;
import demo.onboarding.auth.dto.SignupReqDto;
import demo.onboarding.auth.dto.SignupResDto;
import demo.onboarding.user.User;
import demo.onboarding.user.UserRepository;
import demo.onboarding.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SignupResDto signup(SignupReqDto signupReqDto) {
        String encodedPassword = passwordEncoder.encode(signupReqDto.getPassword());
        if (userRepository.existsByUsername(signupReqDto.getUsername())) {
            throw new IllegalArgumentException("이미 가입한 사용자입니다");
        }
        User user = User.builder().
                username(signupReqDto.getUsername()).
                password(encodedPassword).
                nickname(signupReqDto.getNickname()).
                userRole(UserRole.USER).build();
        userRepository.save(user);
        List<AuthorityDto> authorities = List.of(new AuthorityDto(user.getUserRole().getAuthority()));
        return new SignupResDto(user.getUsername(), user.getNickname(), authorities);
    }
}
