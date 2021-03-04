package uz.bdm.HrTesting.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.LoginDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.jwt.JWTToken;
import uz.bdm.HrTesting.security.jwt.JWTTokenProvider;
import uz.bdm.HrTesting.security.jwt.JwtAuthenticationResponse;
import uz.bdm.HrTesting.service.AuthService;

import java.util.Date;


@Service
public class AuthServiceImpl implements AuthService {

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    public AuthServiceImpl(JWTTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(jwtTokenProvider.createAccessAndRefreshToken(authentication));
    }

    @Override
    public ResponseEntity<?> refreshToken(String token) {
        ResponseData result = jwtTokenProvider.validateRefreshToken(token);

        if (!result.isAccept()) return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

        Authentication authentication = jwtTokenProvider.getRefreshAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JWTToken accessToken = jwtTokenProvider.createAccessToken(authentication);

        Date validityRefreshToken = (Date) result.getData();

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                accessToken.getToken(),
                accessToken.getExpired(),
                token,
                validityRefreshToken.getTime()
        );

        return ResponseEntity.ok(response);
    }
}
