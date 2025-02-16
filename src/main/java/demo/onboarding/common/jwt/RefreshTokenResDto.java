package demo.onboarding.common.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefreshTokenResDto {
    @JsonProperty("access-token")
    private String AccessToken;
    @JsonProperty("refresh-toekn")
    private String RefreshToken;
}
