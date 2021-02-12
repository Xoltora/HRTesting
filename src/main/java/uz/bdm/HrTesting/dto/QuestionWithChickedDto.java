package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionWithChickedDto {

    private String text;
    private List<SelectableAnswerDto> selectableAnswerDtoList;
    private String writtenAnswerText;
    private Boolean right;

}
