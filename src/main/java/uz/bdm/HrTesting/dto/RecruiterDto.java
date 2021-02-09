package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Recruiter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class RecruiterDto {

    private Long id;
    @NotBlank
    private String fio;
    private Date created;

    public Recruiter mapToEntity() {
        Recruiter recruiter = new Recruiter();

        if (this.id != null) recruiter.setId(this.id);

        if (this.fio != null) recruiter.setFio(this.fio);

        if (this.created != null ) recruiter.setCreated(this.created);

        return recruiter;
    }

}
