package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.RecruiterDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.service.RecruiterService;

import javax.validation.Valid;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    public HttpEntity<?> findAllList(){
        ResponseData result = recruiterService.findAll();

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = recruiterService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping
    public HttpEntity<?> save(@Valid @RequestBody RecruiterDto recruiterDto) {
        ResponseData result = recruiterService.save(recruiterDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping
    public HttpEntity<?> update(@Valid @RequestBody RecruiterDto recruiterDto) {
        ResponseData result = recruiterService.update(recruiterDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ResponseData result = recruiterService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
