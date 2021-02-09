package uz.bdm.HrTesting.domain;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.ExamResultDto;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
public class ExamResult extends AuditEntity implements Serializable {

    @ManyToOne
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

//    public ExamResultDto mapToExamResultDto(){
//
//    }
}
