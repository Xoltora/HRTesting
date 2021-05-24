package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.ropository.UserTestRepository;
import uz.bdm.HrTesting.service.StatisticsService;
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserTestRepository userTestRepository;

    public StatisticsServiceImpl(UserTestRepository userTestRepository) {
        this.userTestRepository = userTestRepository;
    }

    @Override
    public ResponseData byWeek() {
        ResponseData result = new ResponseData();
        try {
            Object[] byWeekAgo = userTestRepository.findByWeekAgo();
            for (int i = 0; i < byWeekAgo.length; i++) {
                System.out.println(byWeekAgo[i]);
            }
            result.setAccept(true);
            result.setData(byWeekAgo);
        }catch (Exception e){
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error in gat by week statistics");
        }
        return result;
    }
}
