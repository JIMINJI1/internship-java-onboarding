package demo.onboarding.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.secret-key}")
    private String secretKey;
    private static final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.access-expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${jwt.refresh-expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(String username, String userRole) {
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .claim("username", username)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String generateRefreshToken(String username, String userRole) {
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .claim("username", username)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public boolean validateToken(String token) {
        token = token.replace(BEARER_PREFIX, "");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰입니다");
        } catch (SignatureException e) {
            throw new IllegalArgumentException("유효하지 않는 서명입니다");
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("지원되지 않는 토큰 입니다.");
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userRole", String.class);
    }

    public boolean isTokenExpired(String token) {
        token = token.replace(BEARER_PREFIX, "");
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
