package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.dto.DepartmentDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.service.DepartmentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public HttpEntity<?> findAllList() {
        ResponseData result = departmentService.findAll();

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = departmentService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping
    public HttpEntity<?> saveDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        ResponseData result = departmentService.save(departmentDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping
    public HttpEntity<?> updateDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        ResponseData result = departmentService.update(departmentDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDepartment(@PathVariable Long id) {
        ResponseData result = departmentService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
