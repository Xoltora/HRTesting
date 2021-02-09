package uz.bdm.HrTesting.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.bdm.HrTesting.domain.UserDetail;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String fio;
    private String login;
    private String password;
    private boolean isActive;
    private UserDetail userDetail;
    private List<SimpleGrantedAuthority> authorities;

    public UserPrincipal(Long id, String fio, String login,String password, boolean isActive, List<SimpleGrantedAuthority> authorities) {
        this.id = id;
        this.fio = fio;
        this.login = login;
        this.password = password;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

}
