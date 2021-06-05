package uz.bdm.HrTesting.dto.exam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Exam;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamInfoDto {

    private Long userId;

    private String fio;

    private String testName;

    List<ExamInfoAttemptDto> results;

    public ExamInfoDto mapToDto(Exam exam){
        ExamInfoAttemptDto dto = new ExamInfoAttemptDto();
        dto.setCreated(exam.getCreated());
        dto.setId(exam.getId());
        dto.setCountQuestion(exam.getExamResult().getCountQuestion());
        dto.setCountRight(exam.getExamResult().getCountRight());
        dto.setCountNotAnswered(exam.getExamResult().getCountNotAnswered());
        dto.setPercent(exam.getExamResult().getPercent());
        dto.setNumberOfAttempt(exam.getNumberOfAttempt());
        if (this.getResults() == null){
            this.setResults(new ArrayList<>());
            this.getResults().add(dto);
        } else this.getResults().add(dto);
        return this;
    }
}
