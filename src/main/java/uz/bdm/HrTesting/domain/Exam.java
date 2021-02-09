package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Exam extends AuditEntity implements Serializable {

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
}
