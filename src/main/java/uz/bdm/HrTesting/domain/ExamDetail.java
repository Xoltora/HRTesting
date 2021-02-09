package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ExamDetail extends AuditEntity implements Serializable {

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selectable_ans_id")
    private SelectableAnswer selectableAnswer;

    @Column(name = "written_ans_text")
    private String writtenAnswerText;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;
}
