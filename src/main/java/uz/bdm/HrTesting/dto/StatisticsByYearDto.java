package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StatisticsByYearDto {
    private String name;
    private List<StatisticsMonthDto> monthList;
}
