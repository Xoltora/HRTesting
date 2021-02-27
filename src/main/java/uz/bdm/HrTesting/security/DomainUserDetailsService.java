package uz.bdm.HrTesting.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.domain.UserDetail;
import uz.bdm.HrTesting.exception.controller.DocumentNotFoundException;
import uz.bdm.HrTesting.service.UserDetailService;
import uz.bdm.HrTesting.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    //    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);
    private final UserService userService;
    private final UserDetailService userDetailService;

    public DomainUserDetailsService(UserService userService, UserDetailService userDetailService) {
        this.userService = userService;
        this.userDetailService = userDetailService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        return loadByLogin(login);
    }

    public UserPrincipal loadByLogin(final String login) {
        User user = userService.findByLogin(login);

        if (user == null)
            throw new UsernameNotFoundException("User " + login + " was not found in the database");

        return createUserPrincipal(user);
    }

    private UserPrincipal createUserPrincipal(User user) {
        List<SimpleGrantedAuthority> authorities = getAuthorities(user);

        UserPrincipal userPrincipal = new UserPrincipal(
                user.getId(),
                user.getFio(),
                user.getLogin(),
                user.getPassword(),
                true,
                getAuthorities(user));

        userPrincipal.setLogin(user.getLogin());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setActive(user.getIsActive());

        //TODO util class ochish kerak
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_CANDIDATE"))) {
            UserDetail userDetail = userDetailService.findByUserId(user.getId());
            userPrincipal.setUserDetail(userDetail);
        }
        return userPrincipal;
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

    }
}
