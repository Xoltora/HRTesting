package uz.bdm.HrTesting.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.DomainUserDetailsService;
import uz.bdm.HrTesting.security.UserPrincipal;


import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";

    private Key accessKey;
    private Key refreshKey;

    @Value("${jwt.access.secret.key}")
    private String jwtAccessSecretKey;

    @Value("${jwt.refresh.secret.key}")
    private String jwtRefreshSecretKey;

    @Value("${jwt.access.token.validityInMs}")
    private int accessTokenValidityInMS;

    @Value("${jwt.refresh.token.validityInMs}")
    private int accessTokenValidityInMSForRememberMe;

    @Value("${jwt.refresh.token.validityInMsForRememberMe}")
    private int refreshTokenValidityInMS;

    private final DomainUserDetailsService domainUserDetailsService;

    public JWTTokenProvider(DomainUserDetailsService domainUserDetailsService) {
        this.domainUserDetailsService = domainUserDetailsService;
    }

    @Override
    public void afterPropertiesSet() {

        byte[] jwtAccessSecretKeyByte = DatatypeConverter.parseBase64Binary(jwtAccessSecretKey);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Key jwtAccessSecretKey = new SecretKeySpec(jwtAccessSecretKeyByte, signatureAlgorithm.getJcaName());

        this.accessKey = jwtAccessSecretKey;
        this.refreshKey = jwtAccessSecretKey;
        this.accessTokenValidityInMS *= 1000;
        this.accessTokenValidityInMSForRememberMe *= 1000;
    }


    public JwtAuthenticationResponse createAccessAndRefreshToken(Authentication authentication) {
        JWTToken accessToken = createAccessToken(authentication);
        JWTToken refreshToken = createRefreshToken(authentication);
        return new JwtAuthenticationResponse(
                accessToken.getToken(),
                accessToken.getExpired(),
                refreshToken.getToken(),
                refreshToken.getExpired()
        );
    }

    public JWTToken createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMS);
        ;

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, accessKey)
                .setExpiration(validity)
                .compact();
        return new JWTToken(token, validity.getTime());
    }

    public JWTToken createRefreshToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;

        validity = new Date(now + this.refreshTokenValidityInMS);

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, refreshKey)
                .setExpiration(validity)
                .compact();
        return new JWTToken(token, validity.getTime());
    }

    public Authentication getAccessAuthentication(String token) {
        UserPrincipal userPrincipal = domainUserDetailsService.loadByLogin(getSubjectFromToken(token, accessKey));
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    public Authentication getRefreshAuthentication(String token) {
        UserPrincipal principal = domainUserDetailsService.loadByLogin(getSubjectFromToken(token, refreshKey));
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

//    public String getPasswordKeyByToken(String token) {
//        Claims claims = Jwts.parser()
//            .setSigningKey(refresh)
//            .parseClaimsJws(token)
//            .getBody();
//        return claims.get(PASSWORD_KEY).toString();
//    }

    public String getSubjectFromToken(String token, Key key) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    //TODO message yozish kerak response qaytaradigan qib
    public boolean validateAccessToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(accessKey).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    public ResponseData validateRefreshToken(String authToken) {
        ResponseData result = new ResponseData();
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(authToken);
            result.setAccept(true);
            result.setData(claimsJws.getBody().getExpiration());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT signature.");
//            log.trace("Invalid JWT signature trace: {}", e);
//
            result.setAccept(false);
            result.setMessage("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT token.");
//            log.trace("Expired JWT token trace: {}", e);

            result.setAccept(false);
            result.setMessage("Expired JWT token");
        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token.");
//            log.trace("Unsupported JWT token trace: {}", e);

            result.setAccept(false);
            result.setMessage("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
//            log.info("JWT token compact of handler are invalid.");
//            log.trace("JWT token compact of handler are invalid trace: {}", e);

            result.setAccept(false);
            result.setMessage("JWT token compact of handler are invalid");
        }
        return result;
    }

    private Claims getClaims(String token, Key key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
