package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.RecruiterDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.CurrentUser;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.RecruiterService;
import uz.bdm.HrTesting.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    private final UserService userService;

    public RecruiterController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> findAllList(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findAllRecruiter(userPrincipal);
    }
}
