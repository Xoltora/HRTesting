package uz.bdm.HrTesting.service;

import org.springframework.http.ResponseEntity;
import uz.bdm.HrTesting.dto.LoginDto;

public interface AuthService {

    ResponseEntity<?> authenticateUser(LoginDto loginDto);

    ResponseEntity<?> refreshToken(String token);
}
