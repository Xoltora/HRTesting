package uz.bdm.HrTesting.security.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
    private String type = "Bearer";
    private String accessToken;
    private long accessTokenExpired;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long refreshTokenExpired;

    public JwtAuthenticationResponse(String accessToken, long accessTokenExpired, String refreshToken, long refreshTokenExpired) {
        this.accessToken = accessToken;
        this.accessTokenExpired = accessTokenExpired;
        this.refreshToken = refreshToken;
        this.refreshTokenExpired = refreshTokenExpired;
    }
}
