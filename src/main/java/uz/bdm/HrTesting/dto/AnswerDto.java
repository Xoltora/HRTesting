package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Question;
import uz.bdm.HrTesting.domain.SelectableAnswer;
import uz.bdm.HrTesting.domain.WrittenAnswer;
import uz.bdm.HrTesting.util.View;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AnswerDto {
    @JsonView(value = View.Answer.class)
    private Long id;

    @JsonView(value = View.Answer.class)
    private Long questionId;

    @NotBlank
    @JsonView(value = View.Answer.class)
    private String text;

    @JsonView(value = View.AnswerWithRight.class)
    private Boolean right;

    public SelectableAnswer mapToSelectableAnswer() {
        SelectableAnswer selectableAnswer = new SelectableAnswer();

        if (this.id != null) selectableAnswer.setId(this.id);

        if (this.text != null) selectableAnswer.setText(this.text);

        if (this.questionId != null) selectableAnswer.setQuestion(new Question(this.questionId));

        if (this.right != null) selectableAnswer.setRight(this.right);

        return selectableAnswer;
    }

    public WrittenAnswer mapToWrittenAnswer(){
        WrittenAnswer writtenAnswer = new WrittenAnswer();

        if (this.id != null) writtenAnswer.setId(this.id);

        if (this.questionId != null) writtenAnswer.setQuestion(new Question(this.questionId));

        if (this.text != null) writtenAnswer.setText(this.text);

        return writtenAnswer;
    }

}
