package demo.onboarding.common.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String userId;
    private String refreshToken;
    private String username;

    public void update(String refreshToekn){
        this.refreshToken = refreshToekn;
    }
}
