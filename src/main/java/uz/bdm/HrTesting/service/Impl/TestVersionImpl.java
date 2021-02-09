package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.ropository.ExamRepository;
import uz.bdm.HrTesting.ropository.QuestionRepository;
import uz.bdm.HrTesting.ropository.SelectableAnswerRepository;
import uz.bdm.HrTesting.ropository.TestRepository;
import uz.bdm.HrTesting.service.TestVersion;

@Service
public class TestVersionImpl implements TestVersion {

    private final ExamRepository examRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final SelectableAnswerRepository selectableAnswerRepository;

    public TestVersionImpl(ExamRepository examRepository, TestRepository testRepository, QuestionRepository questionRepository, SelectableAnswerRepository selectableAnswerRepository) {
        this.examRepository = examRepository;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.selectableAnswerRepository = selectableAnswerRepository;
    }

    @Override
    public Boolean checkForUpdateVersion(Long testId) {
        return examRepository.checkExistsTest(testId);
    }


}
