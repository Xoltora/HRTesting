package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.ExamResult;
import uz.bdm.HrTesting.util.View;

import javax.persistence.Column;

@Getter
@Setter
public class ExamResultDto {

    @JsonView(value = View.Result.class)
    private Integer countQuestion=0;

    @JsonView(value = View.Result.class)
    private Integer countRight=0;

    @JsonView(value = View.Result.class)
    private Integer countWrong=0;

    @JsonView(value = View.ResultWithUnchecked.class)
    private Integer countUnchecked=0;

    @JsonView(value = View.Result.class)
    private Integer countNotAnswered=0;

    @JsonView(value = View.Result.class)
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
