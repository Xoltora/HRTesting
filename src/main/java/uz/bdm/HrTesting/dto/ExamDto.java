package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.domain.User;
import java.util.Date;

@Getter
@Setter
public class ExamDto {
    private Long id;
    private User user;
    private Test test;
    private Date started;
    private Date finished;
    private Integer time;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date created;


}
