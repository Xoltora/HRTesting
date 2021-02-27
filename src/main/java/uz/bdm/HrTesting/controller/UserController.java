package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.ChangePasswordDto;
import uz.bdm.HrTesting.dto.RecruiterDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.UserDto;
import uz.bdm.HrTesting.exception.controller.DocumentNotFoundException;
import uz.bdm.HrTesting.security.CurrentUser;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public HttpEntity<?> findAllList(@CurrentUser UserPrincipal userPrincipal,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.findAll(userPrincipal, page, size);
    }

    @GetMapping("/info")
    public HttpEntity<?> info(@CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = userService.getInfo(userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = userService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping
    public HttpEntity<?> save(@Valid @RequestBody UserDto userDto, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = userService.save(userDto, userPrincipal);
        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping
    public HttpEntity<?> update(@Valid @RequestBody UserDto userDto, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = userService.update(userDto, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ResponseData result = userService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/password")
    public HttpEntity<?> updatePassword(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        ResponseData result = userService.updatePassword(userPrincipal, changePasswordDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
