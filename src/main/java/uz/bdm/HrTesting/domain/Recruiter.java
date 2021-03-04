package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.RecruiterDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Recruiter extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "fio")
    private String fio;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public RecruiterDto mapToDto() {
        RecruiterDto recruiterDto = new RecruiterDto();

        recruiterDto.setId(super.getId());
        recruiterDto.setFio(this.fio);
        recruiterDto.setCreated(super.created);

        return recruiterDto;
    }

    public Recruiter(Long id, String fio) {
     super.setId(id);
     this.fio = fio;
    }

}
