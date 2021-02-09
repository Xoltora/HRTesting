package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.AnswerDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.AnswerType;

import javax.transaction.Transactional;
import java.util.List;

public interface AnswerService {

    @Transactional
    ResponseData saveAll(List<AnswerDto> answers, AnswerType answerType,Long testId);
}
