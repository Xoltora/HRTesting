package uz.bdm.HrTesting.service;

import org.springframework.http.ResponseEntity;
import uz.bdm.HrTesting.dto.CandidateDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface CandidateService {

    ResponseData findAll();

    ResponseEntity findAll(List<Long> departmentId, List<Long> recruiterId, Date from, Date to, Integer page, Integer size);

    ResponseData findById(Long id);

    @Transactional
    ResponseData save(CandidateDto candidateDto);

    ResponseData deleteById(Long id);

    @Transactional
    ResponseData update(CandidateDto candidateDto);

}
