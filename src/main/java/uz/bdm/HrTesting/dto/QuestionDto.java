package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Question;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.enums.AnswerType;
import uz.bdm.HrTesting.util.View;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class QuestionDto {
    @JsonView(value = View.Question.class)
    private Long id;

    @NotNull
    @JsonView(value = View.Question.class)
    private Long testId;

    @JsonView(value = View.Question.class)
    private String testName;

    @NotBlank
    @JsonView(value = View.Question.class)
    private String text;

    @NotNull
    @JsonView(value = View.Question.class)
    private AnswerType answerType;

    @JsonView(value = View.Question.class)
    private String image;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String prefix;

    @JsonView(value = View.Question.class)
    private Boolean hasImage = false;

    @JsonView(value = View.QuestionWithAnswer.class)
    private List<AnswerDto> answers;

    @JsonView(value = View.QuestionWithAnswer.class)
    private String writtenAnswerText;

    public Question mapToEntity() {
        Question question = new Question();

        if (this.id != null) question.setId(this.id);

        if (this.text != null) question.setText(this.text);

        if (this.testId != null) question.setTest(new Test(this.testId, this.testName));

        if (this.answerType != null ) question.setAnswerType(this.answerType);

        return question;
    }
}
