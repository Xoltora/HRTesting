package uz.bdm.HrTesting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.service.ExamDetailService;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.util.View;

@RestController
@RequestMapping("/exam")
public class Exam1Controller {

    private final ExamService examService;
    private final ExamDetailService examDetailService;

    public Exam1Controller(ExamService examService, ExamDetailService examDetailService) {
        this.examService = examService;
        this.examDetailService = examDetailService;
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

    @GetMapping("/view/{id}")
    public HttpEntity<?> byId(@PathVariable("id") Long id){
        ResponseData result = examDetailService.getViewById(id);

        if(result.isAccept()){
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
