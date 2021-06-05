package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.*;
import uz.bdm.HrTesting.ropository.UserTestRepository;
import uz.bdm.HrTesting.service.StatisticsService;

import java.math.BigInteger;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserTestRepository userTestRepository;

    public StatisticsServiceImpl(UserTestRepository userTestRepository) {
        this.userTestRepository = userTestRepository;
    }

    @Override
    public ResponseData byWeek() {
        ResponseData<List<StatisticsByWeekDto>> result = new ResponseData<>();
        try {
            List<Object[]> byWeekAgo = userTestRepository.findByWeekAgo();

            List<StatisticsByWeekDto> dtoList = new ArrayList<>();

            StatisticsByWeekDto dto = null;

            List<StatisticsDateDto> dates = null;

            for (int i = 0; i < byWeekAgo.size(); i++) {

                if (i == 0 || !(byWeekAgo.get(i)[0].equals(byWeekAgo.get(i - 1)[0]))){

                    if (i != 0) {
                        dto.setDates(dates);
                        dtoList.add(dto);
                    }
                    dto = new StatisticsByWeekDto();
                    dto.setName((String) byWeekAgo.get(i)[1]);
                    dates = new ArrayList<>();
                    dates.add(new StatisticsDateDto((Date) byWeekAgo.get(i)[2], ((BigInteger) byWeekAgo.get(i)[3]).intValue()));
                } else {
                    dates.add(new StatisticsDateDto((Date) byWeekAgo.get(i)[2], ((BigInteger) byWeekAgo.get(i)[3]).intValue()));
                }
            }
            dto.setDates(dates);
            dtoList.add(dto);

            result.setAccept(true);
            result.setData(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error in gat by week statistics");
        }
        return result;
    }

    @Override
    public ResponseData byYear() {
        ResponseData<List<StatisticsByYearDto>> result = new ResponseData<>();
        try {
            List<Object[]> byYear = userTestRepository.findByYear();

            List<StatisticsByYearDto> byYearList = new ArrayList<>();

            StatisticsByYearDto dto = null;

            List<StatisticsMonthDto> months = null;

            for (int i = 0; i < byYear.size(); i++) {

                if (i == 0 || !(byYear.get(i)[0].equals(byYear.get(i - 1)[0]))){

                    if (i != 0) {
                        dto.setMonthList(months);
                        byYearList.add(dto);
                    }
                    dto = new StatisticsByYearDto();
                    dto.setName((String) byYear.get(i)[0]);
                    months = new ArrayList<>();
                    months.add(new StatisticsMonthDto(((String) byYear.get(i)[1]).trim(), ((BigInteger) byYear.get(i)[2]).intValue()));
                } else {
                    months.add(new StatisticsMonthDto(((String) byYear.get(i)[1]).trim(), ((BigInteger) byYear.get(i)[2]).intValue()));
                }
            }
            dto.setMonthList(months);
            byYearList.add(dto);

            result.setAccept(true);
            result.setData(byYearList);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error in gat by week statistics");
        }
        return result;
    }
}
