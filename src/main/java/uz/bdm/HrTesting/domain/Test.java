package uz.bdm.HrTesting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.test.TestDto;

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
public class Test extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "name", columnDefinition = "Text")
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "version")
    private Integer version;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted = false;

    public Test(Long id) {
        super.setId(id);
    }

    public TestDto mapToDto() {
        TestDto testDto = new TestDto();

        if (super.getId() != null) testDto.setId(super.getId());

        if (super.getCreated() != null) testDto.setCreated(super.getCreated());

        if (this.name != null) testDto.setName(this.name);

        if (this.department != null) {
            testDto.setDepartmentId(this.department.getId());
            testDto.setDepartmentName(this.department.getName());
        }

        if (this.createdBy != null) testDto.setCreatedBy(this.createdBy);

        return testDto;
    }

    public Test(Long id, String name) {
        super.setId(id);
        this.name = name;
    }
}
