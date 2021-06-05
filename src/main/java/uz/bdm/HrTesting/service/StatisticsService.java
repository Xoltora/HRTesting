package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;

public interface StatisticsService {

    ResponseData byWeek();

    ResponseData byYear();
}
