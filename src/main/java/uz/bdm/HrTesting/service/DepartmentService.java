package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.domain.Department;
import uz.bdm.HrTesting.dto.DepartmentDto;
import uz.bdm.HrTesting.dto.ResponseData;

public interface DepartmentService {

    ResponseData save(DepartmentDto departmentDto);

    ResponseData update(DepartmentDto departmentDto);

    ResponseData findById(Long id);

    ResponseData deleteById(Long id);

    ResponseData findAll();
}
