package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.AnswerDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WrittenAnswer extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Question question;

    @Column(name = "text")
    private String text;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public AnswerDto mapToDto(){
        AnswerDto answerDto = new AnswerDto();

        if(this.question != null) answerDto.setQuestionId(this.question.getId());

        if (this.text != null) answerDto.setText(this.text);

        return answerDto;
    }
}
