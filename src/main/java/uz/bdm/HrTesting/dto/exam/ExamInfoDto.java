package uz.bdm.HrTesting.dto.exam;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.dto.ExamResultDto;
import uz.bdm.HrTesting.util.View;

import java.util.Date;

@Getter
@Setter
public class ExamInfoDto {

    @JsonView(value = View.Exam.class)
    private Long id;

    @JsonView(value = View.Exam.class)
    private Long userId;

    @JsonView(value = View.Exam.class)
    private String fio;

    @JsonView(value = View.Exam.class)
    private Long departmentId;

    @JsonView(value = View.Exam.class)
    private String departmentName;

    @JsonView(value = View.Exam.class)
    private String testName;

    @JsonView(value = View.Exam.class)
    private Date created;

    @JsonView(value = View.ExamWithResult.class)
    private Date started;

    @JsonView(value = View.ExamWithResult.class)
    private Date finished;

    @JsonView(value = View.ExamWithResult.class)
    private ExamResultDto examResultDto;




}
