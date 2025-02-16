package demo.onboarding.jwt;

import demo.onboarding.common.jwt.JwtUtil;
import demo.onboarding.user.User;
import demo.onboarding.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;
    private String secretKey = "7J6Q67CUIOuwseyXlOuTnCDqsJzrsJzsnpDqsIAg65CY6rOgabcdefghijklmn";
    private Long accessExp = 1800000L;
    private Long refreshExp = 604800000L;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "ACCESS_TOKEN_EXPIRATION_TIME", accessExp);
        ReflectionTestUtils.setField(jwtUtil, "REFRESH_TOKEN_EXPIRATION_TIME", refreshExp);

        jwtUtil.init();
        user = User.builder()
                .username("이름")
                .nickname("닉네임")
                .userRole(UserRole.USER)
                .password("pwpw123456")
                .build();
    }

    @Test
    void createAccessToken_success() {
        String token = jwtUtil.generateAccessToken(user.getUsername(), user.getUserRole().toString());
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
    }

    @Test
    void createRefreshToken_success() {
        String token = jwtUtil.generateRefreshToken(user.getUsername(), user.getUserRole().toString());
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
    }

    @Test
    void validateToken_success() {
        String token = jwtUtil.generateAccessToken(user.getUsername(), user.getUserRole().toString());
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_failure_expiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "ACCESS_TOKEN_EXPIRATION_TIME", 1);
        String expiredToken = jwtUtil.generateAccessToken(user.getUsername(), user.getUserRole().toString());
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("테스트 중 스레드가 인터럽트되었습니다.");
        }

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> jwtUtil.validateToken(expiredToken)
        );
        assertEquals("만료된 토큰입니다", exception.getMessage());
    }

    @Test
    void validateToken_failure_signatureException() {
        String invalidSignatureToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyTmFtZSIsImlhdCI6MTY0OTI4MDgwMH0.WrongSignature";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jwtUtil.validateToken(invalidSignatureToken);
        });

        assertEquals("유효하지 않는 서명입니다", exception.getMessage());
    }
}
