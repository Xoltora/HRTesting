package uz.bdm.HrTesting.service.Impl;

import org.springframework.core.Constants;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.constants.AuthoritiesConstants;
import uz.bdm.HrTesting.domain.*;
import uz.bdm.HrTesting.dto.CandidateDto;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.BaseDto;
import uz.bdm.HrTesting.enums.ExamState;
import uz.bdm.HrTesting.ropository.ExamRepository;
import uz.bdm.HrTesting.ropository.TestSettingRepository;
import uz.bdm.HrTesting.ropository.UserDetailRepository;
import uz.bdm.HrTesting.ropository.UserRepository;
import uz.bdm.HrTesting.service.CandidateService;
import uz.bdm.HrTesting.service.ExamService;
import uz.bdm.HrTesting.service.UserService;
import uz.bdm.HrTesting.util.HelperFunctions;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final ExamRepository examRepository;
    private final UserService userService;
    private final TestSettingRepository testSettingRepository;
    private final ExamService examService;

    public CandidateServiceImpl(UserRepository userRepository, UserDetailRepository userDetailRepository, ExamRepository examRepository, UserService userService, TestSettingRepository testSettingRepository, ExamService examService) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.examRepository = examRepository;
        this.userService = userService;
        this.testSettingRepository = testSettingRepository;
        this.examService = examService;
    }

    @Override
    public ResponseData findAll() {
        ResponseData result = new ResponseData();

        try {
            result.setAccept(true);
            List<UserDetail> all = userDetailRepository.findAll();

            List<CandidateDto> collect = all.stream()
                    .map(caUserDetail -> caUserDetail.mapToCandidateDto())
                    .collect(Collectors.toList());

            result.setData(collect);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get list ");
        }

        return result;
    }

    @Override
    public ResponseEntity findAll(List<Long> departmentId, List<Long> recruiterId, Date from, Date to, Integer page, Integer size) {

        ResponseData responseData = new ResponseData();
        Pageable pageable = PageRequest.of(page, size);
        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            Page<UserDetail> userDetailPage = userDetailRepository.findAll(departmentId, recruiterId, from, to, pageable);
            httpHeaders.add("page", String.valueOf(userDetailPage.getNumber()));
            httpHeaders.add("size", String.valueOf(userDetailPage.getSize()));
            httpHeaders.add("totalPages", String.valueOf(userDetailPage.getTotalPages()));
            List<CandidateDto> candidateDtoList = userDetailPage.getContent()
                    .stream()
                    .map(userDetail -> userDetail.mapToCandidateDto())
                    .collect(Collectors.toList());
            responseData.setData(candidateDtoList);
            responseData.setAccept(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setAccept(false);
            responseData.setMessage("Проблемь");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(responseData);
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        UserDetail userDetail = userDetailRepository.findByUserId(id).orElse(null);


        if (userDetail == null) {
            result.setAccept(false);
            result.setMessage("Кандидат не найден ID = " + id);
        } else {

            List<BaseDto> baseDtoList = examRepository.findAllTest(userDetail.getUser().getId());

            CandidateDto candidateDto = userDetail.mapToCandidateDto();
            candidateDto.setTests(baseDtoList);
            candidateDto.setLogin(userDetail.getPassSeries());
            candidateDto.setPassword(userDetail.getPassword());
            candidateDto.setPassSeries(userDetail.getPassSeries());

            result.setAccept(true);
            result.setData(candidateDto);
        }
        return result;
    }

    @Override
    @Transactional
    public ResponseData save(CandidateDto candidateDto) {
        ResponseData result = new ResponseData();

        try {

            User user = candidateDto.mapToUserEntity();
            user.setLogin(candidateDto.getPassSeries());

            String password = HelperFunctions.generatePassword(8);
            user.setPassword(HelperFunctions.passwordEncode(password));

            user.setRoles(new HashSet<>(Arrays.asList(new Role(AuthoritiesConstants.CANDIDATE))));

            User save = userRepository.save(user);

            UserDetail userDetail = candidateDto.mapToUserDetailEntity(save);
            userDetail.setPassword(password);

            UserDetail saveUserDetail = userDetailRepository.save(userDetail);

            if (candidateDto.getTests() != null) {
                List<Exam> examList = new ArrayList<>();

                for (BaseDto test : candidateDto.getTests()) {
                    TestSetting testSetting = testSettingRepository.findByTestId(test.getId()).orElse(null);

                    examList.add(new Exam(
                            save,
                            test.mapToEntity(),
                            testSetting.getTime(),
                            ExamState.NOT_STARTED
                    ));
                }

                examRepository.saveAll(examList);
            }
            CandidateDto saveDto = save.mapToCandidateDto(saveUserDetail);

            saveDto.setTests(candidateDto.getTests());
            result.setAccept(true);
            result.setMessage("Кандидат успешно создан !");
            result.setData(saveDto);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage(candidateDto.getPassSeries() + " уже существует в базе данных !");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @Override
    @Transactional
    public ResponseData update(CandidateDto candidateDto) {
        ResponseData result = new ResponseData();

        try {

            UserDetail userDetail = userDetailRepository.findByUserId(candidateDto.getId()).orElse(null);

            if (userDetail == null) {
                result.setAccept(false);
                result.setMessage("Кандидат не найден ID = " + candidateDto.getId());
                return result;
            }

            userDetail.setDepartment(
                    new Department(
                            candidateDto.getDepartmentId(),
                            candidateDto.getDepartmentName()
                    ));

            userDetail.setRecruiter(new Recruiter(
                    candidateDto.getRecruiterId(),
                    candidateDto.getRecruiterFio()
            ));

            userDetail.setPhoneCode(userDetail.getPhoneCode());
            userDetail.setPhoneNum(candidateDto.getPhoneNum());

            User user = userDetail.getUser();
            user.setFio(candidateDto.getFio());

            User saveUser = userRepository.save(user);

            UserDetail saveUserDetail = userDetailRepository.save(userDetail);

            if (candidateDto.getTests() == null) {
                examRepository.deleteAllByUserId(saveUser.getId());
            } else {
                updateChangedTest(saveUser, candidateDto);
            }

            CandidateDto saveDto = saveUser.mapToCandidateDto(saveUserDetail);

            saveDto.setTests(candidateDto.getTests());
            result.setAccept(true);
            result.setMessage("Кандидат успешно создан !");
            result.setData(saveDto);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage(candidateDto.getPassSeries() + " уже существует в базе данных !");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @Override
    public ResponseData deleteById(Long id) {
        ResponseData result = new ResponseData();

        try {
            result = userService.deleteById(id);

            if (result.isAccept())
                result.setMessage("Кандидат успешно удалён !");

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error delete data");
        }

        return result;
    }

    @Transactional
    public void updateChangedTest(User saveUser, CandidateDto candidateDto) {

        List<Long> idsLong = candidateDto.getTests().stream()
                .map(BaseDto::getId).collect(Collectors.toList());

        examRepository.deleteAllByUserIdTestIdNot(saveUser.getId(),
                idsLong
        );

        List<BaseDto> allTest = examRepository.findAllTest(saveUser.getId());

        List<BaseDto> tests = candidateDto.getTests();

        List<Exam> examList = new ArrayList<>();

        for (BaseDto test : tests) {

            boolean match = allTest.stream().noneMatch(testShortDto ->
                    testShortDto.getId().equals(test.getId())
            );

            if (match) {
                TestSetting testSetting = testSettingRepository.findByTestId(test.getId()).orElse(null);

                examList.add(new Exam(
                        saveUser,
                        test.mapToEntity(),
                        testSetting.getTime(),
                        ExamState.NOT_STARTED
                ));
            }
        }

        examRepository.saveAll(examList);
    }

}
