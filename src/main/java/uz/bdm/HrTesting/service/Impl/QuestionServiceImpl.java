package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.domain.Question;
import uz.bdm.HrTesting.domain.SelectableAnswer;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.dto.AnswerDto;
import uz.bdm.HrTesting.dto.QuestionDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.AnswerType;
import uz.bdm.HrTesting.ropository.ExamRepository;
import uz.bdm.HrTesting.ropository.QuestionRepository;
import uz.bdm.HrTesting.ropository.SelectableAnswerRepository;
import uz.bdm.HrTesting.ropository.WrittenAnswerRepository;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.AnswerService;
import uz.bdm.HrTesting.service.FileService;
import uz.bdm.HrTesting.service.QuestionService;
import uz.bdm.HrTesting.service.TestService;
import uz.bdm.HrTesting.util.HelperFunctions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final TestService testService;
    private final FileService fileService;
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;
    private final WrittenAnswerRepository writtenAnswerRepository;
    private final ExamRepository examRepository;
    private final SelectableAnswerRepository selectableAnswerRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public QuestionServiceImpl(QuestionRepository questionRepository, TestService testService, FileService fileService, AnswerService answerService, WrittenAnswerRepository writtenAnswerRepository, ExamRepository examRepository, SelectableAnswerRepository selectableAnswerRepository) {
        this.questionRepository = questionRepository;
        this.testService = testService;
        this.fileService = fileService;
        this.answerService = answerService;
        this.writtenAnswerRepository = writtenAnswerRepository;
        this.examRepository = examRepository;
        this.selectableAnswerRepository = selectableAnswerRepository;
    }

    @Override
    @Transactional
    public ResponseData save(QuestionDto questionDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();


        try {

            Question question = questionDto.mapToEntity();

            if (HelperFunctions.isNotNullOrEmpty(questionDto.getImage())) {

                String fileName = HelperFunctions.randomFileName(questionDto.getPrefix());
                ResponseData responseData = fileService.saveFileToDiscWithByte(fileName, questionDto.getImage());

                if (!responseData.isAccept()) {
                    return responseData;
                }

                question.setImageName(fileName);
                question.setImagePath(String.valueOf(responseData.getData()));
            }

            Question save = questionRepository.save(question);

            if (save.getAnswerType() != AnswerType.WRITTEN) {
                AtomicReference<Integer> countRightAnswer= new AtomicReference<>(0);
                List<SelectableAnswer> answerList =
                        questionDto.getAnswers().stream()
                                .map(answerDto -> {
                                    if (answerDto.getRight()) countRightAnswer.getAndSet(countRightAnswer.get() + 1);

                                    answerDto.setQuestionId(save.getId());
                                    return answerDto.mapToSelectableAnswer();
                                })
                                .collect(Collectors.toList());

                save.setCountRightAnswer(countRightAnswer.get());
                questionRepository.save(save);
                selectableAnswerRepository.saveAll(answerList);
            }

            result.setAccept(true);
            result.setMessage("Вопрос успешно создан !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;

    }

    @Override
    public ResponseData findAll(Long testId) {

        ResponseData result = new ResponseData();

        try {
            List<Question> questions = questionRepository.findAllByTestId(testId);
            List<QuestionDto> questionDtoList = questions.stream()
                    .map(question -> question.mapToDto())
                    .collect(Collectors.toList());

            result.setAccept(true);
            result.setData(questionDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @Override
    public ResponseData findAllWithAnswer(Long testId) {
        ResponseData result = new ResponseData();

        try {
            List<Question> questions = questionRepository.findAllByTestId(testId);
            List<QuestionDto> questionDtoList = questions.stream()
                    .map(question -> {
                        QuestionDto questionDto = question.mapToDto();
                        if (questionDto.getAnswerType() != AnswerType.WRITTEN) {

                            List<SelectableAnswer> byQuestionId = selectableAnswerRepository.findByQuestionIdAndIsDeletedNot(questionDto.getId(), true);

                            List<AnswerDto> collect = byQuestionId.stream()
                                    .map(selectableAnswer -> selectableAnswer.mapToDto())
                                    .collect(Collectors.toList());

                            questionDto.setAnswers(collect);
                        }
                        return questionDto;
                    })
                    .collect(Collectors.toList());

            result.setAccept(true);
            result.setData(questionDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error find data");
        }

        return result;
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            result.setAccept(false);
            result.setMessage("Вопрос не найден ID = " + id);
            return result;
        }

        QuestionDto questionDto = question.mapToDto();

        if (questionDto.getAnswerType() != AnswerType.WRITTEN) {

            List<SelectableAnswer> byQuestionId = selectableAnswerRepository.findByQuestionIdAndIsDeletedNot(questionDto.getId(), true);

            List<AnswerDto> collect = byQuestionId.stream()
                    .map(selectableAnswer -> selectableAnswer.mapToDto())
                    .collect(Collectors.toList());

            questionDto.setAnswers(collect);
        }


        result.setAccept(true);
        result.setData(questionDto);

        return result;
    }

    @Override
    public ResponseData deleteById(Long id) {
        ResponseData result = new ResponseData();

        try {
            questionRepository.deleteById(id);
            result.setAccept(true);
            result.setMessage("Вопрос успешно удалён !");
            result.setData(id);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error delete data");
        }

        return result;
    }

    @Override
    @Transactional
    public ResponseData update(QuestionDto questionDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        Question question = questionRepository.findById(questionDto.getId()).orElse(null);

        if (question == null) {
            result.setAccept(false);
            result.setMessage("Вопрос не найден ID = " + question.getId());
            return result;
        }
        Boolean checkExistsTest = examRepository.checkExistsTest(question.getTest().getId());

        if (checkExistsTest) {
            return updateWithTestVersion(question, questionDto, userPrincipal);
        } else {
            return updateWithoutTestVersion(question, questionDto, userPrincipal);
        }

    }

    @Transactional
    public ResponseData updateWithTestVersion(Question question, QuestionDto questionDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        ResponseData updateVersionResult = testService.updateVersion(question.getTest().getId());

        if (!updateVersionResult.isAccept()) return updateVersionResult;

        try {

            List<Question> allByTestId = questionRepository.findAllByTestId(question.getTest().getId());

            for (Question question1 : allByTestId) {

                if (question1.getId() == question.getId()) {

                    Question question2 = questionDto.mapToEntity();

                    if (HelperFunctions.isNotNullOrEmpty(questionDto.getImage())) {

                        String fileName = HelperFunctions.randomFileName(questionDto.getPrefix());
                        ResponseData responseData = fileService.saveFileToDiscWithByte(fileName, questionDto.getImage());

                        if (!responseData.isAccept()) {
                            return responseData;
                        }

                        question2.setImageName(fileName);
                        question2.setImagePath(String.valueOf(responseData.getData()));
                    }

                    question2.setTest(new Test((Long) updateVersionResult.getData()));
                    question2.setId(null);
                    Question save = questionRepository.save(question2);

                    if (save.getAnswerType() != AnswerType.WRITTEN) {
                        List<SelectableAnswer> answerList =
                                questionDto.getAnswers().stream()
                                        .map(answerDto -> {
                                            answerDto.setId(null);
                                            answerDto.setQuestionId(save.getId());
                                            return answerDto.mapToSelectableAnswer();
                                        })
                                        .collect(Collectors.toList());

                        selectableAnswerRepository.saveAll(answerList);
                    }

                    result.setAccept(true);
                    result.setData(save.mapToDto());

                } else {
                    Long id = question1.getId();

                    question1.setTest(new Test((Long) updateVersionResult.getData()));
                    question1.setId(null);
                    Question save = questionRepository.save(question1);

                    if (question1.getAnswerType() != AnswerType.WRITTEN) {
                        List<SelectableAnswer> answerList = selectableAnswerRepository.findByQuestionIdAndIsDeletedNot(id, true);
                        selectableAnswerRepository.saveAll(
                                answerList.stream()
                                        .map(selectableAnswer -> {
                                            entityManager.detach(selectableAnswer);
                                            selectableAnswer.setQuestion(save);
                                            selectableAnswer.setId(null);
                                            return selectableAnswer;
                                        })
                                        .collect(Collectors.toList()));
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error create new version");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }


    @Transactional
    public ResponseData updateWithoutTestVersion(Question question, QuestionDto questionDto, UserPrincipal userPrincipal) {

        ResponseData result = new ResponseData();

        try {
            question.setText(questionDto.getText());

            if (question.getAnswerType() != questionDto.getAnswerType()) {
                updateChangedAnswerType(question, questionDto);
            } else if (questionDto.getAnswerType() != AnswerType.WRITTEN && questionDto.getAnswers() == null) {
                selectableAnswerRepository.deleteAllByQuestionId(question.getId());
            } else if (questionDto.getAnswerType() != AnswerType.WRITTEN && questionDto.getAnswers() != null) {
                updateChangedAnswers(question, questionDto);
            }

            question.setAnswerType(questionDto.getAnswerType());

            if (HelperFunctions.isNotNullOrEmpty(questionDto.getImage())) {

                String fileName = HelperFunctions.randomFileName(questionDto.getPrefix());
                ResponseData responseData = fileService.saveFileToDiscWithByte(fileName, questionDto.getImage());

                if (!responseData.isAccept()) {
                    return responseData;
                }

                question.setImageName(fileName);
                question.setImagePath(String.valueOf(responseData.getData()));
            }

            Question save = questionRepository.save(question);

            result.setMessage("Вопрос успешно обновлен !");
            result.setAccept(true);
            result.setData(save.mapToDto());

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error update data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @Override
    public void viewImage(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Question not found!");
        }

        if (question.getImageName().endsWith(".jpeg")) {
            response.setContentType("image/jpeg");
        }

        if (question.getImageName().endsWith(".png")) {
            response.setContentType("image/png");
        }

        response.setHeader("Content-Disposition", "inline; filename=" + question.getImageName());

        fileService.imageView(question.getImageName(), question.getImagePath(), request, response);

    }

    @Transactional
    public void updateChangedAnswerType(Question question, QuestionDto questionDto) {

        if (question.getAnswerType() != AnswerType.WRITTEN) {
            selectableAnswerRepository.deleteAllByQuestionId(question.getId());
        }

        if (questionDto.getAnswerType() != AnswerType.WRITTEN && questionDto.getAnswers() != null) {

            List<SelectableAnswer> selectableAnswers = questionDto.getAnswers().stream()
                    .map(answerDto -> {
                        SelectableAnswer selectableAnswer = answerDto.mapToSelectableAnswer();
                        selectableAnswer.setQuestion(question);
                        return selectableAnswer;
                    })
                    .collect(Collectors.toList());

            selectableAnswerRepository.saveAll(selectableAnswers);

            //
//            List<SelectableAnswer> byQuestionId = selectableAnswerRepository.findByQuestionIdAndIsDeletedNot(questionDto.getId(), true);
//
//            List<AnswerDto> collect = byQuestionId.stream()
//                    .map(selectableAnswer -> selectableAnswer.mapToDto())
//                    .collect(Collectors.toList());
//
//            questionDto.setAnswers(collect);
        }

    }

    @Transactional
    public void updateChangedAnswers(Question question, QuestionDto questionDto) {

        List<SelectableAnswer> forSave = new ArrayList<>();
        List<AnswerDto> notNulls = new ArrayList<>();

        for (AnswerDto answer : questionDto.getAnswers()) {
            if (answer.getId() != null) {
                notNulls.add(answer);
            } else {
                SelectableAnswer selectableAnswer = answer.mapToSelectableAnswer();
                selectableAnswer.setQuestion(question);
                forSave.add(selectableAnswer);
            }
        }

        selectableAnswerRepository.deleteByIdNot(notNulls.stream().map(AnswerDto::getId).collect(Collectors.toList()));

        for (AnswerDto answerDto : notNulls) {
            SelectableAnswer selectableAnswer = selectableAnswerRepository.findById(answerDto.getId()).orElse(null);
            if (selectableAnswer != null) {
                selectableAnswer.setText(answerDto.getText());
                selectableAnswer.setRight(answerDto.getRight());

                forSave.add(selectableAnswer);
            }
        }

        selectableAnswerRepository.saveAll(forSave);

    }

}
