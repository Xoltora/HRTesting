package uz.bdm.HrTesting.service.Impl;

import org.springframework.stereotype.Service;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.ropository.ExamResultRepository;
import uz.bdm.HrTesting.service.ReportService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private final ExamResultRepository examResultRepository;

    public ReportServiceImpl(ExamResultRepository examResultRepository) {
        this.examResultRepository = examResultRepository;
    }

    @Override
    public ResponseData findByDate(String from, String to) {
        ResponseData result = new ResponseData();

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Object[]> byDateReport = examResultRepository.findByDateReport(simpleDateFormat.parse(from), simpleDateFormat.parse(to));

            List<Map<String, Object>> data = new ArrayList<>();

            for (Object[] objects : byDateReport) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", objects[0]);
                map.put("fio", objects[1]);
                map.put("countCandidate", objects[2]);
                DecimalFormat df = new DecimalFormat("###.##");
                map.put("middlePercent", df.format(objects[3]));
                data.add(map);
            }

            result.setAccept(true);
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get data");
        }

        return result;
    }

}
