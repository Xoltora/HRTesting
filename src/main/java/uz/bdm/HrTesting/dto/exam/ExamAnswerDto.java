package uz.bdm.HrTesting.dto.exam;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.domain.ExamDetail;
import uz.bdm.HrTesting.domain.Question;
import uz.bdm.HrTesting.domain.SelectableAnswer;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ExamAnswerDto {
    @NotNull
    private Long id;
    @NotNull
    private Long questionId;
    private Long answerId;
    private String answerText;

    public ExamDetail mapToAnswerTextExamDetailEntity(){
        ExamDetail examDetail = new ExamDetail();
        examDetail.setExam(new Exam(this.id));
        examDetail.setQuestion(new Question(this.questionId));
        examDetail.setWrittenAnswerText(answerText);
        return examDetail;
    }


    public ExamDetail mapToAnswerSelectableExamDetailEntity(){
        ExamDetail examDetail = new ExamDetail();
        examDetail.setExam(new Exam(this.id));
        examDetail.setQuestion(new Question(this.questionId));
        examDetail.setSelectableAnswer(new SelectableAnswer(this.answerId));
        return examDetail;
    }

}
