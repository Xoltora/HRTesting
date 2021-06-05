package uz.bdm.HrTesting.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StatisticsByWeekDto {
    private String name;
    private List<StatisticsDateDto> dates;
}
