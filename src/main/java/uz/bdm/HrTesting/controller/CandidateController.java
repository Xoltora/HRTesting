package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.CandidateDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.service.CandidateService;

import javax.validation.Valid;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public HttpEntity<?> findAllList() {
        ResponseData result = candidateService.findAll();

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = candidateService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping
    public HttpEntity<?> save(@Valid @RequestBody CandidateDto candidateDto) {
        ResponseData result = candidateService.save(candidateDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping
    public HttpEntity<?> update(@Valid @RequestBody CandidateDto candidateDto) {
        ResponseData result = candidateService.update(candidateDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ResponseData result = candidateService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
