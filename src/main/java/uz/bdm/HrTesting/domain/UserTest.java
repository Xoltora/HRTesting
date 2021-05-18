package uz.bdm.HrTesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.UserTestDto;

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
public class UserTest extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "number_of_attempts")
    private Integer numberOfAttempts;

    @Column(name = "completed_attempts")
    private Integer completedAttempts;

    @Column(name = "percent")
    private Integer percent;

    public UserTestDto mapToDto(){
        UserTestDto dto = new UserTestDto();

        if (this.user != null) {
            dto.setUser(this.user.mapToDto());
        }

        if (this.test != null){
            dto.setTest(this.test.mapToDto());
        }

        dto.setNumberOfAttempts(this.numberOfAttempts);

        dto.setCompletedAttempts(this.numberOfAttempts - this.completedAttempts);

        return dto;
    }
}
