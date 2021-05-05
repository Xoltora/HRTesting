package uz.bdm.HrTesting.service.Impl;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.domain.ExamResult;
import uz.bdm.HrTesting.dto.ExamResultDto;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.ropository.ExamRepository;
import uz.bdm.HrTesting.ropository.ExamResultRepository;
import uz.bdm.HrTesting.service.FinishExamService;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class FinishExamServiceImpl implements FinishExamService {

    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;

    public FinishExamServiceImpl(ExamRepository examRepository, ExamResultRepository examResultRepository) {
        this.examRepository = examRepository;
        this.examResultRepository = examResultRepository;
    }

    @Async("asyncExecutor")
    @Transactional
    public void finishExam(Exam exam) {
        try {
            Date finishedDate = DateUtils.addMinutes(exam.getStarted(), exam.getTime());
            Date currentDate = new Date();
            System.out.println("started" + new Date());
            Thread.sleep(finishedDate.getTime() - currentDate.getTime());

            exam = examRepository.findById(exam.getId()).orElse(null);

            if (exam.getFinished() == null) {

                Object[] resultExam = examRepository.getResultExam(exam.getId());
                ExamResultDto examResultDto = new ExamResultDto();

                examResultDto.setCountQuestion(Integer.parseInt(String.valueOf(resultExam[0])));

                Integer countMarked = Integer.parseInt(String.valueOf(resultExam[1]));

                examResultDto.setCountRight(Integer.parseInt(String.valueOf(resultExam[2])));
                examResultDto.setCountUnchecked(Integer.parseInt(String.valueOf(resultExam[3])));
                examResultDto.setCountNotAnswered(examResultDto.getCountQuestion() - countMarked - examResultDto.getCountUnchecked());
                examResultDto.setCountWrong(countMarked - examResultDto.getCountRight());

                Double percent = (examResultDto.getCountRight() / (double) examResultDto.getCountQuestion()) * 100;
                examResultDto.setPercent(percent.intValue());

                ExamResult examResult = examResultDto.mapToEntity();
                examResult.setExam(new Exam(exam.getId()));

                examResultRepository.save(examResult);

                exam.setFinished(new Date());

                exam.setState(examResultDto.getCountUnchecked() == 0 ?
                        ExamState.FINISHED : ExamState.MUST_CHECKED);

                examRepository.save(exam);

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error finish THREAD exam id" + exam.getId());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
