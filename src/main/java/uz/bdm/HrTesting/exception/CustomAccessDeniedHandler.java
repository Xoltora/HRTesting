package uz.bdm.HrTesting.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import uz.bdm.HrTesting.dto.CustomError;
import uz.bdm.HrTesting.dto.ErrorResponse;
import uz.bdm.HrTesting.dto.ResponseData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * User: Ismoil
 * Date: 10/14/2020
 * Time: 12:34 PM
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);
        res.getWriter().write(objectMapper.writeValueAsString(new ResponseData(
                false,
                "Извините, но у вас нет доступа"
        )));
    }

}
