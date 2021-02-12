package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.domain.ExamDetail;
import uz.bdm.HrTesting.domain.Question;
import uz.bdm.HrTesting.dto.QuestionWithChickedDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.SelectableAnswerDto;
import uz.bdm.HrTesting.ropository.ExamDetailRepository;
import uz.bdm.HrTesting.ropository.ExamRepository;
import uz.bdm.HrTesting.ropository.QuestionRepository;
import uz.bdm.HrTesting.service.ExamDetailService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ExamDetailServiceImpl implements ExamDetailService {

    private ExamDetailRepository examDetailRepository;
    private QuestionRepository questionRepository;
    private ExamRepository examRepository;

    public ExamDetailServiceImpl(ExamDetailRepository examDetailRepository, QuestionRepository questionRepository, ExamRepository examRepository) {
        this.examDetailRepository = examDetailRepository;
        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
    }

    @Override
    public ResponseData getViewById(Long id) {

        ResponseData responseData = new ResponseData();

        try{
            //List<ExamDetail> examDetailList = examDetailRepository.findByExamIdAndIsDeletedNot(id,false);

            List<Question> questions = questionRepository.findByExamId(id);
            List<QuestionWithChickedDto> selectableAnswerDtos = questions.stream()
                    .map(question -> question.mapToChicked(examDetailRepository
                            .getSelectableAnswerByQuestionId(question.getId()),
                            examDetailRepository.getWrittenTextByQuestionId(id),
                            examDetailRepository.checkAnswer(id))).collect(toList());
            responseData.setAccept(true);
            responseData.setData(selectableAnswerDtos);
        } catch (Exception e){
            responseData.setAccept(false);
            responseData.setMessage("Проблема!");
            e.printStackTrace();
        }
        return responseData;
    }


}
