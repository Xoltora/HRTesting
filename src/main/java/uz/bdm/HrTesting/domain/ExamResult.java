package uz.bdm.HrTesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.ExamResultDto;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExamResult extends AuditEntity implements Serializable {

    @OneToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(name = "count_question")
    private Integer countQuestion;

    @Column(name = "count_right")
    private Integer countRight;

    @Column(name = "count_wrong")
    private Integer countWrong;

    @Column(name = "count_unchecked")
    private Integer countUnchecked;

    @Column(name = "count_not_answered")
    private Integer countNotAnswered;

    @Column(name = "percent")
    private Integer percent;

    public ExamResultDto mapToExamResultDto(){
        ExamResultDto examResultDto = new ExamResultDto();
        examResultDto.setCountNotAnswered(countNotAnswered);
        examResultDto.setCountQuestion(countQuestion);
        examResultDto.setCountRight(countRight);
        examResultDto.setCountUnchecked(countUnchecked);
        examResultDto.setCountWrong(countWrong);
        examResultDto.setPercent(percent);
        examResultDto.setAttempt(this.exam.getNumberOfAttempt());
        return examResultDto;
    }
}
