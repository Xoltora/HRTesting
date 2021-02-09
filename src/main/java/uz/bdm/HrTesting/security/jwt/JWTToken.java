package uz.bdm.HrTesting.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JWTToken {
    private String token;
    private long expired;
}
