package uz.bdm.HrTesting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.LoginDto;
import uz.bdm.HrTesting.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        return authService.authenticateUser(loginDto);
    }

    @PostMapping("/refresh/token/{tokenRefresh}")
    public ResponseEntity<?> refreshToken(@Valid @PathVariable String tokenRefresh) {
        return authService.refreshToken(tokenRefresh);
    }

}
