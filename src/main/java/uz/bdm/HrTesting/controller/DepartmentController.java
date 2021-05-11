package uz.bdm.HrTesting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> findAllList() {
        ResponseData result = departmentService.findAll();

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_RECRUITER')")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = departmentService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_ADMIN')")
    public HttpEntity<?> saveDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        ResponseData result = departmentService.save(departmentDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_ADMIN')")
    public HttpEntity<?> updateDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        ResponseData result = departmentService.update(departmentDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR') or hasAnyAuthority('ROLE_ADMIN')")
    public HttpEntity<?> deleteDepartment(@PathVariable Long id) {
        ResponseData result = departmentService.deleteById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
