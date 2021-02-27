package uz.bdm.HrTesting.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Department;
import uz.bdm.HrTesting.domain.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter

public class TestDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long departmentId;

    private String departmentName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date created;

    private String createdBy;

    public Test mapToEntity(){
        Test test = new Test();

        if (this.id != null) test.setId(this.id);

        if (this.name != null ) test.setName(this.name);

        if (this.departmentId != null ) test.setDepartment(new Department(this.departmentId,this.departmentName));

        if (this.created != null ) test.setCreated(this.created);

        if (this.createdBy != null ) test.setCreatedBy(this.createdBy);

        return test;
    }

}
