package uz.bdm.HrTesting.service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import uz.bdm.HrTesting.domain.Recruiter;
import uz.bdm.HrTesting.domain.Role;
import uz.bdm.HrTesting.domain.User;
import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.UserDto;
import uz.bdm.HrTesting.ropository.UserRepository;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.RoleService;
import uz.bdm.HrTesting.service.UserService;
import uz.bdm.HrTesting.util.HelperFunctions;

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
            User save = userRepository.save(user);

            result.setAccept(true);
            result.setMessage("Пользователь успешно создан !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
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
            userDto.setPassword(user.getPassword());

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
    public ResponseData findAll() {
        ResponseData result = new ResponseData();

        try {
            List<User> all = userRepository.findAll();

            List<UserDto> userDtoList = all.stream()
                    .map(user -> user.mapToDto())
                    .collect(Collectors.toList());

            result.setAccept(true);
            result.setData(userDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get list ");
        }

        return result;
    }

}
