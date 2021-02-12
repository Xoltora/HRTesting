package uz.bdm.HrTesting.dto.exam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.ExamResult;
import uz.bdm.HrTesting.dto.ExamResultDto;
import uz.bdm.HrTesting.util.View;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamInfoDto {

    private Long id;

    private Long userId;

    private String fio;

    private Long departmentId;

    private String departmentName;

    private String testName;

    private Date created;

    private Date started;

    private Date finished;

    private ExamResultDto result;




}
