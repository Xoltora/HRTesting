package uz.bdm.HrTesting.dto.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.dto.ExamResultDto;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamInfoAttemptDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    private Integer countQuestion=0;

    private Integer countRight=0;

    private Integer countWrong=0;

    private Integer countNotAnswered=0;

    private Integer percent=0;

    private Integer numberOfAttempt;
}
