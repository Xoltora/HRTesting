package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.ExamResult;
import uz.bdm.HrTesting.util.View;

import javax.persistence.Column;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamResultDto {

    private Integer countQuestion=0;

    private Integer countRight=0;

    private Integer countWrong=0;

    private Integer countUnchecked=0;

    private Integer countNotAnswered=0;

    private Integer percent=0;

    private String time;

    public ExamResult mapToEntity(){
        ExamResult examResult = new ExamResult();
        examResult.setCountQuestion(this.countQuestion);
        examResult.setCountRight(this.countRight);
        examResult.setCountWrong(this.getCountWrong());
        examResult.setCountUnchecked(this.countUnchecked);
        examResult.setCountNotAnswered(this.countNotAnswered);
        examResult.setPercent(this.percent);
        return examResult;
    }
}
