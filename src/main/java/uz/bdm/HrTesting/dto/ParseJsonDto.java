package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParseJsonDto {
    private Long id;
    private String text;
    private Boolean right_ans;
    private Boolean ismarked;
}
