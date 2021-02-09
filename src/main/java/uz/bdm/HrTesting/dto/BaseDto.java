package uz.bdm.HrTesting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;

    public Test mapToEntity(){
        Test test = new Test();

        if (this.id != null) test.setId(this.id);

        if (this.name != null) test.setName(this.name);

        return test;
    }
}
