package uz.bdm.HrTesting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.audit.AuditEntity;
import uz.bdm.HrTesting.dto.test.TestSettingDto;
import uz.bdm.HrTesting.dto.test.TitleScreenDto;

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
public class TestSetting extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_attempts")
    private Integer numberOfAttempts;

    @Column(name = "time")
    private Integer time;

    public TestSetting(Test test, Integer numberOfAttempts, Integer time) {
        this.test = test;
        this.numberOfAttempts = numberOfAttempts;
        this.time = time;
    }

    public TitleScreenDto mapToTitleScreenDto(){

        TitleScreenDto titleScreenDto = new TitleScreenDto();

        if (this.test != null) titleScreenDto.setTestId(this.test.getId());

        if (this.title != null ) titleScreenDto.setTitle(this.title);

        if (this.description != null) titleScreenDto.setDescription(this.description);

        return titleScreenDto;
    }

    public TestSettingDto mapToDto(){

        TestSettingDto testSettingDto = new TestSettingDto();

        if (this.test != null) testSettingDto.setTestId(this.test.getId());

        if (this.numberOfAttempts != null ) testSettingDto.setNumberOfAttempts(this.numberOfAttempts);

        if (this.time != null) testSettingDto.setTime(this.time);

        return testSettingDto;
    }
}
