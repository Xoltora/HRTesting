package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParseJsonDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("text")
    private String text;
    @JsonProperty("right_ans")
    private Boolean right_ans;
    @JsonProperty("ismarked")
    private Boolean ismarked;
}
