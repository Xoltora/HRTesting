package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.constants.Constants;
import uz.bdm.HrTesting.dto.CandidateDto;
import uz.bdm.HrTesting.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity(name = "u_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "fio", length = 50)
    private String fio;

    @Column(name = "isActive")
    private Boolean isActive;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Role> roles = new HashSet<>();

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public UserDto mapToDto() {
        UserDto userDto = new UserDto();

        if (super.getId() != null) userDto.setId(super.getId());

        if (super.getCreated() != null) userDto.setCreated(super.getCreated());

        if (this.login != null) userDto.setLogin(this.login);

//        if (this.password != null) userDto.setLogin(this.password);

        if (this.fio != null) userDto.setFio(this.fio);

        if (this.isActive != null) userDto.setActive(this.isActive);

        if (this.roles != null)
            userDto.setRoles(this.roles.stream().map(role -> role.getName()).collect(Collectors.toList()));

        return userDto;
    }

    public CandidateDto mapToCandidateDto(UserDetail userDetail) {
        CandidateDto candidateDto = new CandidateDto();

        if (super.getId() != null) candidateDto.setId(super.getId());

        if (super.getCreated() != null) candidateDto.setCreated(super.getCreated());

        if (this.login != null) candidateDto.setLogin(this.login);

        if (userDetail.getPassword() != null) candidateDto.setPassword(userDetail.getPassword());

        if (this.fio != null) candidateDto.setFio(this.fio);

        if (this.isActive != null) candidateDto.setActive(this.isActive);

        if (userDetail.getPassSeries() != null) candidateDto.setPassSeries(userDetail.getPassSeries());

        if (userDetail.getPhoneCode() != null) candidateDto.setPhoneCode(userDetail.getPhoneCode());

        if (userDetail.getPhoneNum() != null) candidateDto.setPhoneNum(userDetail.getPhoneNum());

        if (userDetail.getDepartment() != null) {
            candidateDto.setDepartmentId(userDetail.getDepartment().getId());
            candidateDto.setDepartmentName(userDetail.getDepartment().getName());
        }

        if (userDetail.getRecruiter() != null){
            candidateDto.setRecruiterId(userDetail.getRecruiter().getId());
            candidateDto.setRecruiterFio(userDetail.getRecruiter().getFio());
        }

        return candidateDto;
    }
}
