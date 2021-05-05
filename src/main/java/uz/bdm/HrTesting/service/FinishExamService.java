package uz.bdm.HrTesting.service;

import org.springframework.scheduling.annotation.Async;
import uz.bdm.HrTesting.domain.Exam;

import javax.transaction.Transactional;

public interface FinishExamService {

    @Async("asyncExecutor")
    @Transactional
    void finishExam(Exam exam);
}
