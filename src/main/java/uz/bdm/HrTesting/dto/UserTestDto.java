package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.dto.test.TestDto;

@Getter
@Setter
@AllArgsConstructor
public class UserTestDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto user;

    private TestDto test;

    private Integer numberOfAttempts;

    private Integer completedAttempts;
}
