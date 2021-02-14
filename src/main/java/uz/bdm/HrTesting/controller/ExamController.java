package uz.bdm.HrTesting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.CheckAnswerDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.exam.ExamAnswerDto;
import uz.bdm.HrTesting.security.CurrentUser;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.util.View;

import javax.validation.Valid;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public HttpEntity<?> findAllList(@CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.findAll(userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/info/{id}")
    public HttpEntity<?> info(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.info(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = examService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/start/{examId}")
    @JsonView(value = View.QuestionWithAnswer.class)
    public HttpEntity<?> startExam(@PathVariable Long examId, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.startExam(examId, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/answer")
    public HttpEntity<?> saveAnswer(@RequestBody ExamAnswerDto examAnswerDto, UserPrincipal userPrincipal) {
        ResponseData result = examService.saveAnswer(examAnswerDto, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/finish/{id}")
    public HttpEntity<?> finish(@PathVariable Long id, UserPrincipal userPrincipal) {
        ResponseData result = examService.finish(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/result/by/{id}")
    public HttpEntity<?> resultById(@PathVariable Long id, UserPrincipal userPrincipal) {
        ResponseData result = examService.findResultById(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/report/{id}")
    public HttpEntity<?> reportByExamId(@PathVariable Long id, UserPrincipal userPrincipal) {
        ResponseData result = examService.findReportByExamId(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/question/for-check/{examId}")
    public HttpEntity<?> forCheckQuestion(@PathVariable Long examId, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.findForCheckQuestion(examId, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/check/answer")
    public HttpEntity<?> checkAnswer(@Valid @RequestBody CheckAnswerDto checkAnswerDto, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.checkAnswer(checkAnswerDto, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
