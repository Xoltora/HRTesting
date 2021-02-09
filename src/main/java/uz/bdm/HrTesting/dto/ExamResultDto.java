package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ExamResultDto {

    private Integer countQuestion=0;

    private Integer countRight=0;

    private Integer countWrong=0;

    private Integer countUnchecked=0;

    private Integer countNotAnswered=0;

    private Integer percent=0;
}
