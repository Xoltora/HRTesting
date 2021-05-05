package uz.bdm.HrTesting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * User: Ismoil
 * Date: 9/30/2020
 * Time: 8:31 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private Date timestamp;
    private List<CustomError> errors;

}
