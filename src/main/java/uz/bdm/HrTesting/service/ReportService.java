package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;

public interface ReportService {

    ResponseData findByDate(String from, String to);

}
