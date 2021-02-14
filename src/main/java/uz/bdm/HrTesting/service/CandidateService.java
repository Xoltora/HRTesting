package uz.bdm.HrTesting.service;

import org.springframework.http.ResponseEntity;
import uz.bdm.HrTesting.dto.CandidateDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;
import java.util.Date;

public interface CandidateService {

    ResponseData findAll();

    ResponseEntity findAll(Long departmentId, Long recruiterId, int page, int size);

    ResponseData findById(Long id);

    @Transactional
    ResponseData save(CandidateDto candidateDto);

    ResponseData deleteById(Long id);

    @Transactional
    ResponseData update(CandidateDto candidateDto);

}
