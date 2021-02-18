package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordDto {
    @NotBlank
    private String password;
    @NotBlank
    private String oldPassword;
}
