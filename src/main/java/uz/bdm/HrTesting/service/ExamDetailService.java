package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.enums.ExamState;

public interface ExamDetailService {

    ResponseData getViewById(Long id);

}
