package uz.bdm.HrTesting.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bdm.HrTesting.domain.model.TestFiltr;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.test.TestDto;
import uz.bdm.HrTesting.dto.test.TestSettingDto;
import uz.bdm.HrTesting.dto.test.TitleScreenDto;
import uz.bdm.HrTesting.security.CurrentUser;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.TestService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public  HttpEntity<?> findAll(@RequestParam(value = "from",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                  @RequestParam(value = "to",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
                                  @RequestParam(value = "departmentId",required = false) Long departmentId,
                                  @RequestParam(value = "page",defaultValue = "0") int page,
                                  @RequestParam(value = "size",defaultValue = "10") int size){
        return testService.findAll(departmentId,from,to,page,size);
    }



    @GetMapping("/list/short")
    public HttpEntity<?> findAllShort(){
        ResponseData result = testService.findAllShort();

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Long id) {
        ResponseData result = testService.findById(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping
    public HttpEntity<?> save(@Valid @RequestBody TestDto testDto, @CurrentUser UserPrincipal userPrincipal) {
        ResponseData result = testService.save(testDto, userPrincipal);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public  HttpEntity<?> delete(@PathVariable Long id){
        ResponseData result = testService.delete(id);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/name")
    public  HttpEntity<?> name(@Valid @NotNull Long testId,  @NotBlank @RequestParam String name){
        ResponseData result = testService.updateName(testId, name);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/title/screen/{testId}")
    public HttpEntity<?> titleScreen(@PathVariable Long testId) {
        ResponseData result = testService.findTitleScreenByTestId(testId);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/title/screen")
    public HttpEntity<?> titleScreen(@Valid @RequestBody TitleScreenDto titleScreenDto) {
        ResponseData result = testService.saveTitleScreen(titleScreenDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/title/screen")
    public HttpEntity<?> updateTitleScreen(@Valid @RequestBody TitleScreenDto titleScreenDto) {
        ResponseData result = testService.updateTitleScreen(titleScreenDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/setting")
    public HttpEntity<?> updateSetting(@Valid @RequestBody TestSettingDto testSettingDto) {
        ResponseData result = testService.updateSetting(testSettingDto);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/setting/{testId}")
    public HttpEntity<?> findSettingByTestId(@PathVariable Long testId) {
        ResponseData result = testService.findSettingByTestId(testId);

        if (result.isAccept()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
