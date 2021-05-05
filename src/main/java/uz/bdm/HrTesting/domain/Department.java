package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.DepartmentDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Department extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public Department(Long id, String name) {
        super.setId(id);
        this.name = name;
    }

    public Department(String name) {
        this.name = name;
    }

    public DepartmentDto mapToDto() {
        DepartmentDto departmentDto = new DepartmentDto();

        departmentDto.setId(super.getId());
        departmentDto.setName(this.name);
        departmentDto.setCreated(super.created);

        return departmentDto;
    }
}
