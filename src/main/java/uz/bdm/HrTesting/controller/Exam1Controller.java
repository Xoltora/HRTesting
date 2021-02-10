package uz.bdm.HrTesting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.util.View;

@RestController
@RequestMapping("/exam")
public class Exam1Controller {

    private final ExamService examService;

    public Exam1Controller(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/not-started")
    @JsonView(value = View.Exam.class)
    public HttpEntity<?> findAllNotStarted() {
        ResponseData result = examService.findAllNotStarted();

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable("id") Long id){
        ResponseData result = examService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/not-checked")
    @JsonView(value = View.ExamWithResult.class)
    public HttpEntity<?> findAllNotChecked(){
        ResponseData result = examService.findAllNotChecked();

        if(result.isAccept()){
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


}
