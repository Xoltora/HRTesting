package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.service.ExamService;

@RestController
@RequestMapping("/exam")
public class Exam1Controller {

    private final ExamService examService;

    public Exam1Controller(ExamService examService) {
        this.examService = examService;
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

    @GetMapping("/result/{state}")
    public HttpEntity<?> byState(@PathVariable ExamState state){

        ResponseData result = examService.findByState(state);

        if(result.isAccept()){
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
