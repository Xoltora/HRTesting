package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.exam.ExamAnswerDto;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;

public interface ExamService {

    ResponseData findAll(UserPrincipal userPrincipal);

    ResponseData info(Long id, UserPrincipal userPrincipal);

    ResponseData startExam(Long examId,UserPrincipal userPrincipal);

    ResponseData findById(Long id);

    @Transactional
    ResponseData saveAnswer(ExamAnswerDto examAnswerDto, UserPrincipal userPrincipal);

    ResponseData finish(Long id, UserPrincipal userPrincipal);

    ResponseData findAllNotStarted();

    ResponseData deleteById(Long id);


    ResponseData findAllNotChecked();
}
