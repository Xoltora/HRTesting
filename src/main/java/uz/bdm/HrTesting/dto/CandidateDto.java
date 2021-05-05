package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Department;
import uz.bdm.HrTesting.domain.Recruiter;
import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.domain.UserDetail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CandidateDto {
    private Long id;
    @NotEmpty
    private String fio;

    private String login;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    @JsonIgnore
    private boolean isActive = true;

    private Long departmentId;

    private String departmentName;

    private Long recruiterId;

    private String recruiterFio;

    @NotEmpty
    private String passSeries;
    @NotNull
    private Integer phoneCode;
    @NotNull
    private Integer phoneNum;

    private List<BaseDto> tests;

    public User mapToUserEntity() {
        User user = new User();
        user.setIsActive(this.isActive);

        if (this.id != null) user.setId(this.id);

        if (this.fio != null) user.setFio(this.fio);

        if (this.login != null) user.setLogin(this.login);

        if (this.password != null) user.setPassword(this.password);

        if (this.login != null) user.setLogin(this.login);

        if (this.created != null) user.setCreated(this.created);


        return user;
    }

    public UserDetail mapToUserDetailEntity(User user) {
        UserDetail userDetail = new UserDetail();
        userDetail.setUser(user);

        if (this.passSeries != null) userDetail.setPassSeries(this.passSeries);

        if (this.phoneCode != null) userDetail.setPhoneCode(this.phoneCode);

        if (this.phoneNum != null) userDetail.setPhoneNum(this.phoneNum);

        if (this.departmentId != null) userDetail.setDepartment(new Department(this.departmentId, this.departmentName));

        if (this.recruiterId != null) userDetail.setRecruiter(new User(this.recruiterId, this.recruiterFio));

        return userDetail;
    }


}
