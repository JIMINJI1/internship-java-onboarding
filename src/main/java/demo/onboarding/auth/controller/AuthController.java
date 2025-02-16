package demo.onboarding.auth.controller;

import demo.onboarding.auth.dto.SigninReqDto;
import demo.onboarding.auth.dto.SigninResDto;
import demo.onboarding.auth.dto.SignupReqDto;
import demo.onboarding.auth.dto.SignupResDto;
import demo.onboarding.auth.service.AuthService;
import demo.onboarding.common.jwt.RefreshTokenResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name="Auth API", description= "회원가입/로그인/Refresh Token API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupReqDto));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인하고 JWT 토큰을 받습니다.")
    @PostMapping("/signin")
    public ResponseEntity<SigninResDto> signin(@Valid @RequestBody SigninReqDto signinReqDto) {
        SigninResDto signinResDto = authService.signin(signinReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(signinResDto);
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 이용해 새로운 액세스 토큰을 발급받습니다.")
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResDto> refreshToekn(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToekn(request, response));
    }
}
