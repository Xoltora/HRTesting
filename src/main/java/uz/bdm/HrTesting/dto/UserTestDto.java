package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.dto.test.TestDto;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTestDto {
    private Long id;

    private String fio;

    private String testName;

    private String departmentName;

    private Integer numberOfAttempts;

    private Integer completedAttempts;

    private Date created;
}
