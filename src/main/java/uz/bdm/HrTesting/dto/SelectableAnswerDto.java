package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Question;

@Getter
@Setter
public class SelectableAnswerDto {

    private String text;
    private Boolean right;
    private Boolean selected;

    public SelectableAnswerDto(String text, Boolean right, Boolean selected) {
        this.text = text;
        this.right = right;
        this.selected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SelectableAnswerDto)) {
            return false;
        }
        SelectableAnswerDto s = (SelectableAnswerDto) obj;
        return this.text.equals(s.text);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }
}
