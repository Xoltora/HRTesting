package uz.bdm.HrTesting.service;

import org.springframework.http.ResponseEntity;
import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.dto.ChangePasswordDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.UserDto;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;


public interface UserService {

    User findByLogin(String login);

    ResponseData getInfo(UserPrincipal userPrincipal);

    @Transactional
    ResponseData save(UserDto userDto, UserPrincipal userPrincipal);

    ResponseData update(UserDto userDto, UserPrincipal userPrincipal);

    ResponseData findById(Long id);

    ResponseData deleteById(Long id);

    ResponseEntity findAll(UserPrincipal userPrincipal,Integer page,Integer size);

    ResponseData updatePassword(UserPrincipal userPrincipal, ChangePasswordDto changePasswordDto);

}
