package demo.onboarding.auth.service;

import demo.onboarding.auth.dto.*;
import demo.onboarding.common.jwt.JwtUtil;
import demo.onboarding.common.jwt.RefreshToken;
import demo.onboarding.common.jwt.RefreshTokenRepository;
import demo.onboarding.common.jwt.RefreshTokenResDto;
import demo.onboarding.user.User;
import demo.onboarding.user.UserRepository;
import demo.onboarding.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public SigninResDto signin(SigninReqDto signinReqDto) {
        User user = userRepository.findByUsername(signinReqDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 유저입니다."));

        if (!passwordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getUserRole().toString()).substring(7);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getUserRole().toString()).substring(7);

        RefreshToken savedRefreshToken = new RefreshToken(user.getId().toString(), refreshToken, user.getUsername());
        refreshTokenRepository.save(savedRefreshToken);

        return new SigninResDto(accessToken);
    }

    public RefreshTokenResDto refreshToekn(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("리프레시 토큰이 없습니다");
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        String userRole = jwtUtil.getUserRoleFromToken(refreshToken);
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다");
        }

        RefreshToken savedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다"));

        String newAccessToken = jwtUtil.generateAccessToken(username, userRole).substring(7);
        String newRefreshToken = jwtUtil.generateRefreshToken(username, userRole).substring(7);

        savedRefreshToken.update(newRefreshToken);
        refreshTokenRepository.save(savedRefreshToken);

        return new RefreshTokenResDto(newAccessToken, newRefreshToken);
    }
}
