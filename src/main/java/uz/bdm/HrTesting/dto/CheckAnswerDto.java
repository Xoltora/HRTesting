package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CheckAnswerDto {
    @NotNull
    private Long id;
    @NotNull
    private Long questionId;
    @NotNull
    private Boolean right;
}
