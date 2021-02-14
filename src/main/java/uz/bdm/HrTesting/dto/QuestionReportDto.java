package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.enums.AnswerType;

import java.util.List;

@Getter
@Setter
public class QuestionReportDto {
    private Long id;

    private String text;

    private Boolean hasImage = false;

    private Boolean right;

    private AnswerType answerType;

    private String writtenAnswerText;

    private List<AnswerDto> answers;
}
