package uz.bdm.HrTesting.service.Impl;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.domain.*;
import uz.bdm.HrTesting.dto.AnswerDto;
import uz.bdm.HrTesting.dto.BaseDto;
import uz.bdm.HrTesting.dto.QuestionDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.exam.ExamAnswerDto;
import uz.bdm.HrTesting.enums.AnswerType;
import uz.bdm.HrTesting.ropository.*;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.service.QuestionService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamDetailRepository examDetailRepository;
    private final TestSettingRepository testSettingRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final SelectableAnswerRepository selectableAnswerRepository;


    public ExamServiceImpl(ExamRepository examRepository, ExamDetailRepository examDetailRepository, TestSettingRepository testSettingRepository, QuestionRepository questionRepository, QuestionService questionService, SelectableAnswerRepository selectableAnswerRepository) {
        this.examRepository = examRepository;
        this.examDetailRepository = examDetailRepository;
        this.testSettingRepository = testSettingRepository;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.selectableAnswerRepository = selectableAnswerRepository;
    }

    @Override
    public ResponseData findAll(UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {

            List<Exam> allTest = examRepository.findAllTestByUserId(userPrincipal.getId());

            List<BaseDto> collect = allTest.stream().map(
                    exam -> new BaseDto(
                            exam.getId(),
                            exam.getTest().getName()
                    )
            ).collect(Collectors.toList());

            result.setAccept(true);
            result.setData(collect);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("error get data");
        }

        return result;
    }

    @Override
    public ResponseData info(Long id, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        Map<String, Object> test = new HashMap<>();

        Exam exam = examRepository.findById(id).orElse(null);

        if (exam == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не найден ID = " + id);
            return result;
        }

        if (exam.getStarted() == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не начался ID = " + id);
            return result;
        }

        if (exam.getFinished() != null) {
            result.setAccept(false);
            result.setMessage("Экзамен окончен!");
            return result;
        }

        try {

            List<Question> questions = questionRepository.findAllByTestId(exam.getTest().getId());

            List<QuestionDto> questionDtoList = questions.stream()
                    .map(question -> {
                        QuestionDto questionDto = question.mapToDto();
                        if (questionDto.getAnswerType() != AnswerType.WRITTEN) {

                            List<SelectableAnswer> byQuestionId = selectableAnswerRepository.findByQuestionIdAndIsDeletedNot(questionDto.getId(), true);

                            List<AnswerDto> collect = byQuestionId.stream()
                                    .map(selectableAnswer -> {
                                        AnswerDto answerDto = selectableAnswer.mapToDto();
                                        answerDto.setRight(null);
                                        return answerDto;
                                    })
                                    .collect(Collectors.toList());

                            questionDto.setAnswers(collect);
                        }
                        return questionDto;
                    })
                    .collect(Collectors.toList());


            List<ExamDetail> byExamIdAndIsDeletedNot = examDetailRepository.findByExamIdAndIsDeletedNot(exam.getId(), true);


            int count = 0;
            for (QuestionDto questionDto : questionDtoList) {

                for (ExamDetail examDetail : byExamIdAndIsDeletedNot) {
                    if (questionDto.getId().equals(examDetail.getQuestion().getId())) {
                        if (questionDto.getAnswerType() == AnswerType.WRITTEN) {
                            questionDtoList.get(count).setWrittenAnswerText(examDetail.getWrittenAnswerText());
                        } else {
                            int count1 = 0;
                            for (AnswerDto answer : questionDto.getAnswers()) {
                                if (answer.getId().equals(examDetail.getSelectableAnswer().getId())) {
                                    questionDtoList.get(count).getAnswers().get(count1).setRight(true);
                                }
                                count1++;
                            }
                        }
                    }
                }
                count++;
            }

            test.put("id", id);
            test.put("time", exam.getTime());
            test.put("questions", questionDtoList);
            test.put("started", exam.getStarted());
            Date finishedDate = DateUtils.addMinutes(exam.getStarted(), exam.getTime());
            Date currentDate = new Date();
            Long inMs = (finishedDate.getTime() - currentDate.getTime()) / 1000;
            test.put("timeLeft", (inMs / 60) + ":" + (inMs % 60));

            result.setAccept(true);
            result.setData(test);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("error get data");
        }

        return result;
    }

    @Override
    public ResponseData startExam(Long examId, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        Map<String, Object> test = new HashMap<>();

        Exam exam = examRepository.findById(examId).orElse(null);

        if (exam == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не найден ID = " + examId);
            return result;
        }

        try {
            ResponseData allWithAnswer = questionService.findAllWithAnswer(exam.getTest().getId());

            if (!allWithAnswer.isAccept()) return allWithAnswer;

            test.put("id", examId);
            test.put("time", exam.getTime());
            test.put("questions", allWithAnswer.getData());

            Date currentDate = new Date();
            test.put("started", currentDate);

            exam.setStarted(currentDate);
            examRepository.save(exam);

            result.setAccept(true);
            result.setData(test);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("error saving data ");
        }

        return result;
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        Exam exam = examRepository.findById(id).orElse(null);

        if (exam == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не найден ID = " + id);
            return result;
        }

        try {
            Map<String, Object> test = new HashMap<>();
            TestSetting testSetting = testSettingRepository.findByTestId(exam.getTest().getId()).orElse(null);

            test.put("id", exam.getId());
            test.put("name", testSetting.getTitle());
            test.put("description", testSetting.getDescription());
            test.put("time", exam.getTime());

            result.setData(test);
            result.setAccept(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("error get data");
        }

        return result;
    }

    @Override
    public ResponseData saveAnswer(ExamAnswerDto examAnswerDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        Exam exam = examRepository.findById(examAnswerDto.getId()).orElse(null);

        if (exam == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не найден ID = " + examAnswerDto.getId());
            return result;
        }

        if (exam.getFinished() != null) {
            result.setAccept(false);
            result.setMessage("Экзамен окончен!");
            return result;
        }

        try {
            ResponseData questionById = questionService.findById(examAnswerDto.getQuestionId());
            if (!questionById.isAccept()) return questionById;

            QuestionDto questionDto = (QuestionDto) questionById.getData();

            if (questionDto.getAnswerType() == AnswerType.WRITTEN) {
                examDetailRepository.save(examAnswerDto.mapToAnswerTextExamDetailEntity());
            } else {
                examDetailRepository.save(examAnswerDto.mapToAnswerSelectableExamDetailEntity());
            }

            result.setAccept(true);
            result.setData(examAnswerDto);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error save answer");
        }

        return result;
    }

    @Override
    public ResponseData finish(Long id, UserPrincipal userPrincipal) {

        ResponseData result = new ResponseData();

        Exam exam = examRepository.findById(id).orElse(null);

        if (exam == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не найден ID = " + id);
            return result;
        }

        if (exam.getStarted() == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не начался ID = " + id);
            return result;
        }

        if (exam.getFinished() != null) {
            result.setAccept(false);
            result.setMessage("Экзамен окончен!");
            return result;
        }

        try {

            Integer countQuestion = 0;
            Integer countRight = 0;
            Integer countWrong = 0;
            Integer countUnchecked = 0;
            Integer countNotAnswered = 0;
            Integer percent = 0;


            List<Question> questions = questionRepository.findAllByTestId(exam.getTest().getId());
            List<ExamDetail> examDetailList = examDetailRepository.findByExamIdAndIsDeletedNot(exam.getId(), true);


            countQuestion = questions.size();


//            for (Question question : questions) {
//
//                if (question.getAnswerType() == AnswerType.WRITTEN) {
//                    boolean match = examDetailList.stream().allMatch(examDetail -> examDetail.getQuestion().getId().equals(question.getId()));
//                    if (match) countUnchecked++;
//                    else countNotAnswered++;
//
//                } else if (question.getAnswerType() == AnswerType.ONE_CORRECT) {
//                    boolean hasValue = false;
//
//                    for (ExamDetail examDetail : examDetailList) {
//                        if (examDetail.getQuestion().getId().equals(question.getId())){
//
//                        }
//                    }
//
//                } else {
//
//
//                }

//            }

            result.setAccept(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error save data");
        }
        return result;
    }

}
