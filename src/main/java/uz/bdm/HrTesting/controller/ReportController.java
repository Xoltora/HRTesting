package uz.bdm.HrTesting.controller;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.service.ReportService;

import java.util.Date;

@RestController
@RequestMapping(("/report"))
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public HttpEntity<?> report(@RequestParam(value = "from") String from,
                                @RequestParam(value = "to") String  to) {

        ResponseData result = reportService.findByDate(from, to);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
