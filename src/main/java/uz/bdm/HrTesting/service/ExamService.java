package uz.bdm.HrTesting.service;

import org.springframework.http.ResponseEntity;
import uz.bdm.HrTesting.dto.CheckAnswerDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.exam.ExamAnswerDto;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;
import java.util.Date;

public interface ExamService {

    ResponseData findAll(UserPrincipal userPrincipal);

    ResponseData info(Long id, UserPrincipal userPrincipal);

    ResponseData startExam(Long examId, UserPrincipal userPrincipal);

    ResponseData findById(Long id);

    @Transactional
    ResponseData saveAnswer(ExamAnswerDto examAnswerDto, UserPrincipal userPrincipal);

    @Transactional
    ResponseData finish(Long id, UserPrincipal userPrincipal);

    ResponseData deleteById(Long id);

    ResponseEntity findByState(ExamState examState, Long id, String fio, Date from, Date to, Integer page, Integer size);

    ResponseData findResultById(Long id, UserPrincipal userPrincipal);

    ResponseData findReportByExamId(Long id, UserPrincipal userPrincipal);

    ResponseData findForCheckQuestion(Long examId, UserPrincipal userPrincipal);

    @Transactional
    ResponseData checkAnswer(CheckAnswerDto checkAnswerDto, UserPrincipal userPrincipal);
}
