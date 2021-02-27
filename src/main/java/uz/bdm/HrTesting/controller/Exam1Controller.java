package uz.bdm.HrTesting.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.service.ExamService;

import java.util.Date;

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
    public HttpEntity<?> byState(@PathVariable ExamState state,
                                 @RequestParam(value = "departmentId",required = false) Long departmetId,
                                 @RequestParam(value = "from",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                 @RequestParam(value = "to",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
                                 @RequestParam(value = "page",defaultValue = "0") Integer page,
                                 @RequestParam(value = "size",defaultValue = "10") Integer size){
        return examService.findByState(state,departmetId,from,to,page,size);
    }

}
