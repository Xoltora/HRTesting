package uz.bdm.HrTesting.service.Impl;

import com.google.gson.Gson;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.domain.*;
import uz.bdm.HrTesting.dto.*;
import uz.bdm.HrTesting.dto.exam.ExamAnswerDto;
import uz.bdm.HrTesting.dto.exam.ExamInfoDto;
import uz.bdm.HrTesting.enums.AnswerType;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.ropository.*;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.service.FinishExamService;
import uz.bdm.HrTesting.service.QuestionService;
import uz.bdm.HrTesting.util.HelperFunctions;

import javax.transaction.Transactional;
import java.math.BigInteger;
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
    private final ExamResultRepository examResultRepository;
    private final FinishExamService finishExamService;
    private final TestRepository testRepository;
    private final UserTestRepository userTestRepository;

    public ExamServiceImpl(ExamRepository examRepository, ExamDetailRepository examDetailRepository, TestSettingRepository testSettingRepository, QuestionRepository questionRepository, QuestionService questionService, SelectableAnswerRepository selectableAnswerRepository, ExamResultRepository examResultRepository, FinishExamService finishExamService, TestRepository testRepository, UserTestRepository userTestRepository) {
        this.examRepository = examRepository;
        this.examDetailRepository = examDetailRepository;
        this.testSettingRepository = testSettingRepository;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.selectableAnswerRepository = selectableAnswerRepository;
        this.examResultRepository = examResultRepository;
        this.finishExamService = finishExamService;
        this.testRepository = testRepository;
        this.userTestRepository = userTestRepository;
    }

    @Override
    public ResponseData findAll(UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {

//            List<UserTest> tests1 = userTestRepository.findAllByStateNotProcess(userPrincipal.getId());
//
//            List<Map<String, Object>> collect1 = tests1.stream().map(
//                    userTest -> {
//                        Map<String, Object> map = new LinkedHashMap<>();
//                        map.put("id", userTest.getId());
//                        map.put("name", userTest.getTest().getName());
//                        map.put("numberOfAttempts", userTest.getNumberOfAttempts());
//                        map.put("completedAttempts", userTest.getCompletedAttempts());
////                        map.put("state", exam.getState());
//                        return map;
//                    }
//            ).collect(Collectors.toList());

            List<UserTest> allTest = userTestRepository.findAllByStateNotStarted(userPrincipal.getId());

            List<Map<String, Object>> collect = allTest.stream().map(
                    userTest -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("id", userTest.getId());
                        map.put("name", userTest.getTest().getName());
                        map.put("numberOfAttempts", userTest.getNumberOfAttempts());
                        map.put("completedOfAttempts", userTest.getNumberOfAttempts() - userTest.getCompletedAttempts());
                        map.put("percent", userTest.getPercent());
//                        map.put("state", ExamState.NOT_STARTED);
                        return map;
                    }
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
            result.setMessage(" Время для тестирования закончилось!");
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
            test.put("name", exam.getTest().getName());
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
    public ResponseData startExam(Long testId, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        Map<String, Object> test = new HashMap<>();

//        Exam exam = examRepository.findById(examId).orElse(null);
        Test test1 = testRepository.findById(testId).orElse(null);

        TestSetting testSetting = testSettingRepository.findByTestId(testId).orElse(null);

        if (test1 == null || testSetting == null) {
            result.setAccept(false);
            result.setMessage("Экзамен не найден ID = " + testId);
            return result;
        }

        try {
            ResponseData allWithAnswer = questionService.findAllWithAnswer(testId);

            if (!allWithAnswer.isAccept()) return allWithAnswer;

            UserTest userTest = userTestRepository.findByTestAndUser(test1, userPrincipal.getUserDetail().getUser());

            if (userTest.getNumberOfAttempts().equals(userTest.getCompletedAttempts())){
                result.setAccept(false);
                result.setMessage("У Вас больше нет попытки ");
                return result;
            }

            userTest.setCompletedAttempts(userTest.getCompletedAttempts() + 1);

            Exam exam = new Exam(userPrincipal.getUserDetail().getUser(),
                    test1,
                    testSetting.getTime(),
                    ExamState.ON_PROCESS);
            exam.setNumberOfAttempt(userTest.getCompletedAttempts());

            Date currentDate = new Date();

            exam.setStarted(currentDate);

            Exam savedExam = examRepository.save(exam);

            test.put("id", savedExam.getId());
            test.put("time", savedExam.getTime());
            test.put("questions", allWithAnswer.getData());
            test.put("name", savedExam.getTest().getName());
            test.put("started", currentDate);

            finishExamService.finishExam(exam);

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
    @Transactional
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
            result.setMessage(" Время для тестирования закончилось!");
            return result;
        }

        try {
            ResponseData questionById = questionService.findById(examAnswerDto.getQuestionId());
            if (!questionById.isAccept()) return questionById;

            QuestionDto questionDto = (QuestionDto) questionById.getData();

            if (questionDto.getAnswerType() == AnswerType.WRITTEN) {

                Boolean checkExistAnswerWritten = examDetailRepository.checkExistAnswerWritten(exam.getId(), questionDto.getId());

                if (checkExistAnswerWritten && HelperFunctions.isNotNullOrEmpty(examAnswerDto.getAnswerText())) {
                    examDetailRepository.deleteByExamIdAndQuestionId(exam.getId(), questionDto.getId());
                } else if (HelperFunctions.isNotNullOrEmpty(examAnswerDto.getAnswerText())) {
                    examDetailRepository.save(examAnswerDto.mapToAnswerTextExamDetailEntity());
                }else {
                    examDetailRepository.deleteByExamIdAndQuestionId(exam.getId(), questionDto.getId());
                }

            } else if (questionDto.getAnswerType() == AnswerType.ONE_CORRECT) {
                examDetailRepository.deleteByExamIdAndQuestionId(exam.getId(), questionDto.getId());

                ExamDetail examDetail = examAnswerDto.mapToAnswerSelectableExamDetailEntity();
                examDetail.setRight(selectableAnswerRepository.findRightById(examAnswerDto.getAnswerId()));
                examDetailRepository.save(examDetail);

            } else {
                Boolean checkExistAnswer = examDetailRepository.checkExistAnswer(exam.getId(), questionDto.getId(), examAnswerDto.getAnswerId());
                if (checkExistAnswer) {
                    examDetailRepository.deleteByExamIdAndQuestionIdAndSelectableId(exam.getId(), questionDto.getId(), examAnswerDto.getAnswerId());
                } else {
                    ExamDetail examDetail = examAnswerDto.mapToAnswerSelectableExamDetailEntity();
                    examDetail.setRight(selectableAnswerRepository.findRightById(examAnswerDto.getAnswerId()));
                    examDetailRepository.save(examDetail);
                }
            }

        result.setAccept(true);
        result.setData(examAnswerDto);
    } catch(
    Exception e)

    {
        e.printStackTrace();
        result.setAccept(false);
        result.setMessage("Error save answer");
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

        return result;
}

    @Override
    @Transactional
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
            result.setMessage("Время для тестирования закончилось!");
            return result;
        }

        try {

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

            UserTest userTest = userTestRepository.findByTestAndUser(exam.getTest(), exam.getUser());


            /*
             UserTest entity ga percent save qilmoqda
             */
            userTest.setPercent(percent > userTest.getPercent() ? percent.intValue() : userTest.getPercent());

            userTestRepository.save(userTest);

            examResultRepository.save(examResult);

            Date currentDate = new Date();
            exam.setFinished(currentDate);

            exam.setState(examResultDto.getCountUnchecked() == 0 ?
                    ExamState.FINISHED : ExamState.MUST_CHECKED);

            examRepository.save(exam);

            Long inMs = (currentDate.getTime() - exam.getStarted().getTime()) / 1000;
            examResultDto.setTime((inMs / 60) + ":" + (inMs % 60));

            result.setAccept(true);
            result.setData(examResultDto);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get result");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public ResponseData deleteById(Long id) {

        ResponseData responseData = new ResponseData();

        try {
            examRepository.deleteById(id);
            responseData.setAccept(true);
            responseData.setMessage("Экзамен успешно удалён !");
        } catch (Exception e) {
            responseData.setAccept(false);
            responseData.setMessage("Проблема!");
        }
        return responseData;
    }


    @Override
    public ResponseEntity findByState(ExamState examState, Long id, String fio, Date from, Date to, Integer page, Integer size) {
        ResponseData responseData = new ResponseData();
        Pageable pageable = PageRequest.of(page, size);
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("page",String.valueOf(page));
//        httpHeaders.add("size",String.valueOf(size));
        try {
            if (examState == ExamState.NOT_STARTED){
                Page<UserTest> userTests = userTestRepository.findByNotStarted(id, fio, from, to, pageable);
                httpHeaders.add("page", String.valueOf(userTests.getNumber()));
                httpHeaders.add("size", String.valueOf(userTests.getSize()));
                httpHeaders.add("totalPages", String.valueOf(userTests.getTotalPages()));

                List<UserTestDto> userTestDtoList = userTests.getContent()
                        .stream()
                        .map(userTest -> userTest.mapToDto())
                        .collect(Collectors.toList());
                responseData.setAccept(true);
                responseData.setData(userTestDtoList);
                return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(responseData);
            }
            Page<Exam> examPage = examRepository.findByState(examState, id, fio, from, to, pageable);
            httpHeaders.add("page", String.valueOf(examPage.getNumber()));
            httpHeaders.add("size", String.valueOf(examPage.getSize()));
            httpHeaders.add("totalPages", String.valueOf(examPage.getTotalPages()));
            List<ExamInfoDto> examDtoList = examPage.getContent()
                    .stream()
                    .map(exam -> exam.mapToExamInfoDto())
                    .collect(Collectors.toList());
            responseData.setAccept(true);
            responseData.setData(examDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setAccept(false);
            responseData.setMessage("Проблемь");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(responseData);
    }

    @Override
    public ResponseData findResultById(Long id, Integer attemptNumber, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();
        try {

            Exam exam = examRepository.findByTestAndUserAndNumberOfAttempt(id, userPrincipal.getId(), attemptNumber);

            if (exam == null) {
                result.setAccept(false);
                result.setMessage("Тест не найден ID = " + id);
                return result;
            }

            ExamResult examResult = examResultRepository.findByExamId(exam.getId()).orElse(null);

            if (examResult == null) {
                result.setAccept(false);
                result.setMessage("Результат экзамена не найден ID = " + id);
                return result;
            }

            result.setData(examResult.mapToExamResultDto());
            result.setAccept(true);
        } catch (Exception e) {
            result.setAccept(false);
            result.setMessage("Error find!");
        }
        return result;
    }

    @Override
    public ResponseData findReportByExamId(Long id, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {
//            Exam exam = examRepository.findByTestAndUserAndNumberOfAttempt(id, userId, attemptNumber);
            List<Object[]> reportByExamId = examRepository.findReportByExamId(id);
            List<QuestionReportDto> questionReportDtoList = new ArrayList<>();

            for (Object[] objects : reportByExamId) {
                QuestionReportDto question = new QuestionReportDto();
                BigInteger qId = (BigInteger) objects[0];
                question.setId(qId.longValue());
                question.setAnswerType(AnswerType.valueOf(String.valueOf(objects[1])));
                question.setText(String.valueOf(objects[3]));
                question.setHasImage(HelperFunctions.isNotNullOrEmpty(String.valueOf(objects[4]))
                        && HelperFunctions.isNotNullOrEmpty(String.valueOf(objects[5])));

                Boolean isSelected = null;

                if (question.getAnswerType() == AnswerType.WRITTEN) {

                    String s = String.valueOf(objects[2]);

                    if (!s.equals("null")) {
                        isSelected = true;
                        question.setWrittenAnswerText(s);
                    }


                } else {
                    Gson gson = new Gson();

                    List<ParseJsonDto> parseJsonDto = Arrays.asList(gson.fromJson(String.valueOf(objects[2]), ParseJsonDto[].class));

                    List<AnswerDto> answers = new ArrayList<>();
                    for (ParseJsonDto jsonDto : parseJsonDto) {
                        AnswerDto answerDto = new AnswerDto();

                        answerDto.setId(jsonDto.getId());
                        answerDto.setText(jsonDto.getText());
                        answerDto.setRight(jsonDto.getIsmarked());

                        if (jsonDto.getIsmarked() && isSelected == null) {
                            isSelected = true;
                        }
                        answers.add(answerDto);
                    }
                    question.setAnswers(answers);
                }
                question.setRight(isSelected != null ? ((Boolean) objects[6]) : null);
                questionReportDtoList.add(question);
            }

            result.setData(questionReportDtoList);
            result.setAccept(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get report");
        }

        return result;
    }

    @Override
    public ResponseData findForCheckQuestion(Long examId, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {
            List<ExamDetail> allForCheckQuestion = examDetailRepository.findAllForCheckQuestion(examId);
            List<Map<String, Object>> data = new ArrayList<>();

            for (ExamDetail examDetail : allForCheckQuestion) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", examDetail.getExam().getId());
                map.put("questionId", examDetail.getQuestion().getId());
                map.put("text", examDetail.getQuestion().getText());
                map.put("hasImage", (HelperFunctions.isNotNullOrEmpty(examDetail.getQuestion().getImageName())
                        && HelperFunctions.isNotNullOrEmpty(examDetail.getQuestion().getImagePath())));
                map.put("answer", examDetail.getWrittenAnswerText());

                data.add(map);
            }

            result.setData(data);
            result.setAccept(true);
        } catch (Exception e) {
            result.setAccept(false);
            result.setMessage("Error find questions!");
        }

        return result;
    }

    @Override
    @Transactional
    public ResponseData checkAnswer(CheckAnswerDto checkAnswerDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {
            ExamDetail byExamIdAndIsDeletedNot = examDetailRepository.findByExamIdAndQuestionIdAndIsDeletedNot(checkAnswerDto.getId(), checkAnswerDto.getQuestionId(), true).orElse(null);

            if (byExamIdAndIsDeletedNot == null) {
                result.setAccept(false);
                result.setMessage("Вопрос не найден ID = " + checkAnswerDto.getQuestionId());
                return result;
            }

            if (byExamIdAndIsDeletedNot.getRight() != null) {
                result.setAccept(false);
                result.setMessage("Этот вопрос был проверен ID = " + checkAnswerDto.getQuestionId());
                return result;
            }

            byExamIdAndIsDeletedNot.setRight(checkAnswerDto.getRight());

            examDetailRepository.save(byExamIdAndIsDeletedNot);
            Exam exam = examRepository.findById(checkAnswerDto.getId()).orElse(null);

            ExamResult examResult = exam.getExamResult();
            examResult.setCountUnchecked(examResult.getCountUnchecked() - 1);

            if (checkAnswerDto.getRight()) {
                examResult.setCountRight(examResult.getCountRight() + 1);
                Double percent = (examResult.getCountRight() / (double) examResult.getCountQuestion()) * 100;
                examResult.setPercent(percent.intValue());
            } else {
                examResult.setCountWrong(examResult.getCountWrong() + 1);
            }

            examResultRepository.save(examResult);

            if (examResult.getCountUnchecked() == 0) {
                exam.setState(ExamState.FINISHED);
                examRepository.save(exam);
            }

            result.setMessage("Успешно сохранено !!");
            result.setAccept(true);
        } catch (Exception e) {
            result.setAccept(false);
            result.setMessage("Error find questions!");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public ResponseData findResultById(Long id, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {
            List<ExamResult> examResultList = examResultRepository.findAllByExamId(id, userPrincipal.getId());

            result.setData(examResultList
                    .stream()
                    .map(ExamResult::mapToExamResultDto)
                    .collect(Collectors.toList()));
            result.setAccept(true);
        }catch (Exception e){
            result.setMessage("Error find!");
            result.setAccept(false);
        }
        return result;
    }


}
