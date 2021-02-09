package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.domain.Recruiter;
import uz.bdm.HrTesting.dto.RecruiterDto;
import uz.bdm.HrTesting.dto.ResponseData;

import javax.transaction.Transactional;

public interface RecruiterService {

    ResponseData save(RecruiterDto recruiterDto);

    ResponseData update(RecruiterDto recruiterDto);

    ResponseData findById(Long id);

    ResponseData deleteById(Long id);

    ResponseData findAll();
}
