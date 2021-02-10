package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.exam.ExamInfoDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Exam extends AuditEntity implements Serializable {

    @OneToOne(mappedBy = "exam")
    private ExamResult examResult;

    @ManyToOne
    private User user;

    @ManyToOne
    private Test test;


    @Column(name = "started")
    private Date started;

    @Column(name = "finished")
    private Date finished;

    @Column(name = "time")
    private Integer time;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public Exam(User user, Test test,Integer time) {
        this.user = user;
        this.test = test;
        this.time = time;
    }

    public Exam(Long id) {
        super.setId(id);
    }

    public ExamInfoDto mapToExamInfoDto(){
        ExamInfoDto examInfoDto = new ExamInfoDto();
        examInfoDto.setCreated(super.getCreated());
        examInfoDto.setDepartmentId(test.getDepartment().getId());
        examInfoDto.setFio(user.getFio());
        examInfoDto.setDepartmentName(test.getDepartment().getName());
        examInfoDto.setStarted(started);
        examInfoDto.setFinished(finished);
        examInfoDto.setId(super.getId());
        examInfoDto.setTestName(test.getName());
        //examInfoDto.setExamResultDto(examResult.mapToExamResultDto());
        return examInfoDto;
    }
}
