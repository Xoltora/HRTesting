package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.domain.Department;
import uz.bdm.HrTesting.dto.DepartmentDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.ropository.DepartmentRepository;
import uz.bdm.HrTesting.service.DepartmentService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public ResponseData save(DepartmentDto departmentDto) {
        ResponseData result = new ResponseData();

        Department department = departmentDto.mapToEntity();


        try {
            Department save = departmentRepository.save(department);
            result.setAccept(true);
            result.setMessage("Отдел успешно создан !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData update(DepartmentDto departmentDto) {

        ResponseData result = new ResponseData();

        Department department = departmentRepository.findById(departmentDto.getId()).orElse(null);

        if (department == null) {
            result.setAccept(false);
            result.setMessage("Отдел не найден ID = " + departmentDto.getId());
            return result;
        }

        try {

            departmentDto.setCreated(department.getCreated());
            Department save = departmentRepository.save(departmentDto.mapToEntity());

            result.setAccept(true);
            result.setMessage("Отдел успешно обновлен !");
            result.setData(save.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        Department department = departmentRepository.findById(id).orElse(null);

        if (department == null) {
            result.setAccept(false);
            result.setMessage("Отдел не найден ID = " + id);
        } else {
            result.setAccept(true);
            result.setData(department.mapToDto());
        }
        return result;
    }

    @Override
    public ResponseData deleteById(Long id) {
        ResponseData result = new ResponseData();

        try {
            departmentRepository.deleteById(id);
            result.setAccept(true);
            result.setMessage("Отдел успешно удалён !");
            result.setData(id);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error delete data");
        }

        return result;
    }

    @Override
    public ResponseData findAll() {
        ResponseData result = new ResponseData();

        try {
            result.setAccept(true);
            result.setData(departmentRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get list ");
        }

        return result;
    }
}
