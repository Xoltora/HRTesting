package uz.bdm.HrTesting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.CheckAnswerDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.exam.ExamAnswerDto;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.security.CurrentUser;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.util.View;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> findAllList(@CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.findAll(userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> info(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.info(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = examService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> deleteById(@PathVariable("id") Long id){
        ResponseData result = examService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/result/{state}")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> byState(@PathVariable ExamState state,
                                 @RequestParam(value = "departmentId",required = false) Long departmentId,
                                 @RequestParam(value = "fio",required = false) String fio,
                                 @RequestParam(value = "from",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                 @RequestParam(value = "to",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
                                 @RequestParam(value = "page",defaultValue = "0") Integer page,
                                 @RequestParam(value = "size",defaultValue = "10") Integer size){
        return examService.findByState(state, departmentId,fio,from,to,page,size);
    }

    @GetMapping("/start/{testId}")
    @JsonView(value = View.QuestionWithAnswer.class)
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE')")
    public HttpEntity<?> startExam(@PathVariable Long testId, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.startExam(testId, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/answer")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE')")
    public HttpEntity<?> saveAnswer(@RequestBody ExamAnswerDto examAnswerDto, UserPrincipal userPrincipal) {
        ResponseData result = examService.saveAnswer(examAnswerDto, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/finish/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE')")
    public HttpEntity<?> finish(@PathVariable Long id, UserPrincipal userPrincipal) {
        ResponseData result = examService.finish(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

//    @GetMapping("/result/by/{id}/{attemptNumber}")
//    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
//    public HttpEntity<?> resultById(@PathVariable Long id, @PathVariable("attemptNumber") Integer attemptNumber,  UserPrincipal userPrincipal) {
//        ResponseData result = examService.findResultById(id, attemptNumber, userPrincipal);
//
//        if (result.isAccept()) {
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
//        }
//    }

    @GetMapping("/report/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> reportByExamId(@PathVariable Long id, UserPrincipal userPrincipal) {
        ResponseData result = examService.findReportByExamId(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/question/for-check/{examId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> forCheckQuestion(@PathVariable Long examId, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.findForCheckQuestion(examId, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/check/answer")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> checkAnswer(@Valid @RequestBody CheckAnswerDto checkAnswerDto, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = examService.checkAnswer(checkAnswerDto, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


    @GetMapping("/result/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATE') or hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> resultTestById(@PathVariable Long id, UserPrincipal userPrincipal) {
        ResponseData result = examService.findResultById(id, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
