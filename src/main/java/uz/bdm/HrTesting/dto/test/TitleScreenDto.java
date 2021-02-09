package uz.bdm.HrTesting.dto.test;

import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.TestSetting;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TitleScreenDto {

    @NotNull
    private Long testId;

    @NotBlank
    private String title;

    @NotNull
    private String description;

    public TestSetting mapToEntity(TestSetting testSetting) {
        if (this.title != null) testSetting.setTitle(this.title);

        if (this.description != null ) testSetting.setDescription(this.description);

        return testSetting;
    }

}
