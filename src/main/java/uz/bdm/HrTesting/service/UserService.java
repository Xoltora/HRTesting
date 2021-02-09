package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.UserDto;
import uz.bdm.HrTesting.security.UserPrincipal;


public interface UserService {

    User findByLogin(String login);

    ResponseData getInfo(UserPrincipal userPrincipal);

    ResponseData save(UserDto userDto, UserPrincipal userPrincipal);

    ResponseData update(UserDto userDto, UserPrincipal userPrincipal);

    ResponseData findById(Long id);

    ResponseData deleteById(Long id);

    ResponseData findAll();

}
