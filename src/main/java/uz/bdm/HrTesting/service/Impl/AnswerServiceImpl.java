package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.domain.SelectableAnswer;
import uz.bdm.HrTesting.domain.WrittenAnswer;
import uz.bdm.HrTesting.dto.AnswerDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.AnswerType;
import uz.bdm.HrTesting.ropository.SelectableAnswerRepository;
import uz.bdm.HrTesting.ropository.WrittenAnswerRepository;
import uz.bdm.HrTesting.service.AnswerService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class AnswerServiceImpl implements AnswerService {

    private final WrittenAnswerRepository writtenAnswerRepository;
    private final SelectableAnswerRepository selectableAnswerRepository;

    public AnswerServiceImpl(WrittenAnswerRepository writtenAnswerRepository, SelectableAnswerRepository selectableAnswerRepository) {
        this.writtenAnswerRepository = writtenAnswerRepository;
        this.selectableAnswerRepository = selectableAnswerRepository;
    }

    @Override
    @Transactional
    public ResponseData saveAll(List<AnswerDto> answers, AnswerType answerType, Long testId) {
        ResponseData result = new ResponseData();

        try {


            if (answerType == AnswerType.WRITTEN) {
                List<WrittenAnswer> writtenAnswers = answers.stream()
                        .map(answerDto -> answerDto.mapToWrittenAnswer())
                        .collect(Collectors.toList());

                List<WrittenAnswer> saveAll = writtenAnswerRepository.saveAll(writtenAnswers);
                 result.setData(saveAll);

            } else {
                List<SelectableAnswer> answerList =
                        answers.stream()
                                .map(answerDto -> answerDto.mapToSelectableAnswer())
                                .collect(Collectors.toList());

                List<SelectableAnswer> saveAll = selectableAnswerRepository.saveAll(answerList);
                result.setData(saveAll);
            }

            result.setAccept(true);
            result.setMessage("Варианты успешно создан !");
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error save Answer");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }
}
