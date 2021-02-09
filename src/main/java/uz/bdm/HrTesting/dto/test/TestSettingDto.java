package uz.bdm.HrTesting.dto.test;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.TestSetting;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TestSettingDto {
    @NotNull
    private Long testId;

    @NotNull
    private Integer numberOfAttempts;

    @NotNull
    private Integer time;

    public TestSetting mapToEntity(TestSetting testSetting) {
        if (this.numberOfAttempts != null) testSetting.setNumberOfAttempts(this.numberOfAttempts);

        if (this.time != null) testSetting.setTime(this.time);

        return testSetting;
    }
}
