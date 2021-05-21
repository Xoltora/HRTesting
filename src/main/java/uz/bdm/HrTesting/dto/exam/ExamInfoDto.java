package uz.bdm.HrTesting.dto.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.domain.ExamResult;
import uz.bdm.HrTesting.dto.ExamResultDto;
import uz.bdm.HrTesting.util.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamInfoDto {

    private Long userId;

    private String fio;

    private Long departmentId;

    private String departmentName;

    private String testName;

    List<ExamInfoAttemptDto> dtoList;

    public ExamInfoDto mapToDto(Exam exam){
        ExamInfoAttemptDto dto = new ExamInfoAttemptDto();
        dto.setCreated(exam.getCreated());
        dto.setId(exam.getId());
        dto.setFinished(exam.getFinished());
        dto.setStarted(exam.getStarted());
        dto.setResult(exam.getExamResult().mapToExamResultDto());
        dto.setNumberOfAttempt(exam.getNumberOfAttempt());
        if (this.getDtoList() == null){
            this.setDtoList(new ArrayList<>());
            this.getDtoList().add(dto);
        } else this.getDtoList().add(dto);
        return this;
    }
}
