package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Recruiter;
import uz.bdm.HrTesting.domain.Role;
import uz.bdm.HrTesting.domain.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    @NotEmpty
    private String fio;
    private String login;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    @NotNull
    private List<String> roles;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date created;
//    @JsonIgnore
    private Boolean isActive;

    public User mapToEntity(){
        User user = new User();

        if (this.id != null) user.setId(this.id);

        if (this.fio != null) user.setFio(this.fio);

        if (this.login != null ) user.setLogin(this.login);

        if (this.password != null ) user.setPassword(this.password);

        if (this.login != null ) user.setLogin(this.login);

        if (this.created != null) user.setCreated(this.created);

        if (this.isActive != null) user.setIsActive(this.isActive);

        return user;
    }

}
