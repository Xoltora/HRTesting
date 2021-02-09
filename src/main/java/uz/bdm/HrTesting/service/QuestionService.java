package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.AnswerDto;
import uz.bdm.HrTesting.dto.QuestionDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

public interface QuestionService {
    @Transactional
    ResponseData save(QuestionDto questionDto, UserPrincipal userPrincipal);

    ResponseData findAll(Long testId);

    ResponseData findAllWithAnswer(Long testId);

    ResponseData findById(Long id);

    ResponseData deleteById(Long id);

    @Transactional
    ResponseData update(QuestionDto questionDto, UserPrincipal userPrincipal);

    void viewImage(Long id, HttpServletRequest request,  HttpServletResponse response) throws IOException;

}
