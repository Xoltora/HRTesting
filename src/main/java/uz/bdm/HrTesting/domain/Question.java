package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.QuestionDto;
import uz.bdm.HrTesting.enums.AnswerType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "text",columnDefinition = "Text")
    private String text;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "count_right_ans")
    private Integer countRightAnswer;

    @OneToMany(mappedBy = "question")
    private Set<SelectableAnswer> selectableAnswers;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public QuestionDto mapToDto(){
        QuestionDto questionDto = new QuestionDto();

        if (super.getId() != null) questionDto.setId(super.getId());

        if (this.test != null) {
            questionDto.setTestId(this.test.getId());
            questionDto.setTestName(this.test.getName());
        }

        if (this.text != null) questionDto.setText(this.text);

        if (this.answerType != null) questionDto.setAnswerType(this.answerType);

        if (this.imagePath != null && this.getImageName() != null) questionDto.setHasImage(true);

        return questionDto;
    }

    public Question(Long id) {
        super.setId(id);
    }
}
