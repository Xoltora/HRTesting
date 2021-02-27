package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Department;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class DepartmentDto {
    private Long id;
    @NotBlank
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    public Department mapToEntity() {
        Department department = new Department();

        if (this.id != null) department.setId(this.id);

        if (this.name != null) department.setName(this.name);

        if (this.created != null ) department.setCreated(this.created);

        return department;
    }
}
