package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.CandidateDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;

public interface CandidateService {

    ResponseData findAll();

    ResponseData findById(Long id);

    @Transactional
    ResponseData save(CandidateDto candidateDto);

    ResponseData deleteById(Long id);

    @Transactional
    ResponseData update(CandidateDto candidateDto);

}
