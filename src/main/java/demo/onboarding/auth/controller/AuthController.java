package demo.onboarding.auth.controller;

import demo.onboarding.auth.dto.SignupReqDto;
import demo.onboarding.auth.dto.SignupResDto;
import demo.onboarding.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupReqDto));
    }
}
