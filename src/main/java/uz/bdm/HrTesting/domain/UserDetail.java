package uz.bdm.HrTesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.CandidateDto;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Recruiter recruiter;

    @Column(name = "password")
    private String password;

    @Column(name = "pass_series", unique = true)
    private String passSeries;

    @Column(name = "phone_code")
    private Integer phoneCode;


    @Column(name = "phone_num")
    private Integer phoneNum;

    public CandidateDto mapToCandidateDto() {
        CandidateDto candidateDto = new CandidateDto();

        if (this.user != null) {
            candidateDto.setId(this.user.getId());
            candidateDto.setFio(this.user.getFio());
            candidateDto.setCreated(this.user.getCreated());
            candidateDto.setActive(this.user.getIsActive());
        }

        if (this.phoneCode != null) candidateDto.setPhoneCode(this.phoneCode);

        if (this.phoneNum != null) candidateDto.setPhoneNum(this.phoneNum );

        if (this.department != null) {
            candidateDto.setDepartmentId(this.department.getId());
            candidateDto.setDepartmentName(this.department.getName());
        }

        if (this.recruiter != null) {
            candidateDto.setRecruiterId(this.recruiter.getId());
            candidateDto.setRecruiterFio(this.recruiter.getFio());
        }

        return candidateDto;
    }

}
