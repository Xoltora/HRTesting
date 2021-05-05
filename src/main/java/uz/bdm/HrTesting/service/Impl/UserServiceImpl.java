package uz.bdm.HrTesting.service.Impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.constants.AuthoritiesConstants;
import uz.bdm.HrTesting.domain.Role;
import uz.bdm.HrTesting.domain.User;
import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.ChangePasswordDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.UserDto;
import uz.bdm.HrTesting.ropository.UserRepository;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.RoleService;
import uz.bdm.HrTesting.service.UserService;
import uz.bdm.HrTesting.util.HelperFunctions;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Override
    public ResponseData getInfo(UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();
        UserDto userDto = new UserDto();

        userDto.setId(userPrincipal.getId());
        userDto.setFio(userPrincipal.getFio());
        userDto.setLogin(userPrincipal.getLogin());
        userDto.setRoles(userPrincipal.getAuthorities().stream()
                .map(simpleGrantedAuthority -> simpleGrantedAuthority.getAuthority())
                .collect(Collectors.toList()));

        result.setAccept(true);
        result.setData(userDto);

        return result;
    }

    @Override
    @Transactional
    public ResponseData save(UserDto userDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();
        User user = userDto.mapToEntity();

        try {

            Set<Role> roles = new HashSet<>();
            for (String role : userDto.getRoles()) {
                ResponseData byName = roleService.findByName(role);

                if (!byName.isAccept()) return result;
                roles.add((Role) byName.getData());
            }

            user.setRoles(roles);
            user.setPassword(HelperFunctions.passwordEncode(userDto.getPassword()));
            user.setIsActive(true);
            User save = userRepository.save(user);

            result.setAccept(true);
            result.setMessage("Пользователь успешно создан !");
            result.setData(save.mapToDto());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage(userDto.getLogin() + " уже существует в базе данных !");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }

        return result;
    }

    @Override
    public ResponseData update(UserDto userDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        User user = userRepository.findById(userDto.getId()).orElse(null);

        if (user == null) {
            result.setAccept(false);
            result.setMessage("Пользователь не найден ID = " + userDto.getId());
        }

        try {
            Set<Role> roles = new HashSet<>();
            for (String role : userDto.getRoles()) {
                ResponseData byName = roleService.findByName(role);

                if (!byName.isAccept()) return result;
                roles.add((Role) byName.getData());
            }

            userDto.setCreated(user.getCreated());
            userDto.setLogin(user.getLogin());
            userDto.setPassword(userDto.getPassword() != null ?
                    HelperFunctions.passwordEncode(userDto.getPassword()) : user.getPassword());

            User user1 = userDto.mapToEntity();
            user1.setRoles(roles);

            User save = userRepository.save(user1);

            result.setAccept(true);
            result.setMessage("Пользователь успешно обновлен !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            result.setAccept(false);
            result.setMessage("Пользователь не найден ID = " + id);
        } else {
            result.setAccept(true);
            result.setData(user.mapToDto());
        }
        return result;
    }

    @Override
    public ResponseData deleteById(Long id) {
        ResponseData result = new ResponseData();

        try {
            userRepository.deleteById(id);
            result.setAccept(true);
            result.setMessage("Пользователь успешно удалён !");
            result.setData(id);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error delete data");
        }

        return result;
    }

    @Override
    public ResponseEntity findAll(UserPrincipal userPrincipal, Integer page, Integer size) {
        ResponseData result = new ResponseData();
        HttpHeaders httpHeaders = new HttpHeaders();
        Pageable pageable = PageRequest.of(page, size);
        try {

            ResponseData byName = roleService.findByName(AuthoritiesConstants.CANDIDATE);
//            Set<Role> set = new HashSet(Arrays.asList(byName.getData()));
            Page<User> all = userRepository.findAllNotId(4L, pageable);

            httpHeaders.add("page", String.valueOf(all.getNumber()));
            httpHeaders.add("size", String.valueOf(all.getSize()));
            httpHeaders.add("totalPages", String.valueOf(all.getTotalPages()));
            List<UserDto> userDtoList = all.getContent().stream()
                    .map(user -> user.mapToDto())
                    .collect(Collectors.toList());

            result.setAccept(true);
            result.setData(userDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get list ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(result);
    }

    @Override
    public ResponseEntity findAllRecruiter(UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {
            List<User> all = userRepository.findAllRecruiter(userPrincipal.getId());

            List<UserDto> userDtoList = all.stream()
                    .map(user -> user.mapToDto())
                    .collect(Collectors.toList());

            result.setAccept(true);
            result.setData(userDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get list ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseData updatePassword(UserPrincipal userPrincipal, ChangePasswordDto changePasswordDto) {
        ResponseData result = new ResponseData();

        try {

            User user = userRepository.findByLogin(userPrincipal.getLogin()).orElse(null);
            boolean matchPassword = HelperFunctions.matchPassword(user.getPassword(), changePasswordDto.getOldPassword());

            if (!matchPassword) {
                result.setAccept(false);
                result.setMessage("Неверный старый пароль !");
                return result;
            }

            user.setPassword(HelperFunctions.passwordEncode(changePasswordDto.getPassword()));
            userRepository.save(user);

            result.setAccept(true);
            result.setMessage("Пароль успешно изменен");
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error update password ");
        }

        return result;
    }

}
