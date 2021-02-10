package uz.bdm.HrTesting.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.domain.TestSetting;
import uz.bdm.HrTesting.domain.model.TestFiltr;
import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.test.TestDto;
import uz.bdm.HrTesting.dto.test.TestSettingDto;
import uz.bdm.HrTesting.dto.BaseDto;
import uz.bdm.HrTesting.dto.test.TitleScreenDto;
import uz.bdm.HrTesting.ropository.TestRepository;
import uz.bdm.HrTesting.ropository.TestSettingRepository;
import uz.bdm.HrTesting.security.UserPrincipal;
import uz.bdm.HrTesting.service.TestService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TestSettingRepository testSettingRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public TestServiceImpl(TestRepository testRepository, TestSettingRepository testSettingRepository) {
        this.testRepository = testRepository;
        this.testSettingRepository = testSettingRepository;
    }

    @Override
    @Transactional
    public ResponseData save(TestDto testDto, UserPrincipal userPrincipal) {
        ResponseData result = new ResponseData();

        try {
            testDto.setCreatedBy(userPrincipal.getFio());
            Test test = testDto.mapToEntity();
            test.setVersion(1);

            Test save = testRepository.save(test);

            TestSetting testSetting = new TestSetting(
                    new Test(save.getId()),
                    1,
                    10
            );

            testSettingRepository.save(testSetting);

            result.setAccept(true);
            result.setMessage("Тест успешно создан !");
            result.setData(save.mapToDto());

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @Override
    public ResponseData saveTitleScreen(TitleScreenDto titleScreenDto) {
        ResponseData result = new ResponseData();
        Test test = testRepository.findById(titleScreenDto.getTestId()).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + titleScreenDto.getTestId());
            return result;
        }

        try {
            TestSetting testSetting = testSettingRepository.findByTestId(titleScreenDto.getTestId()).orElse(null);
            testSetting = titleScreenDto.mapToEntity(testSetting);

            TestSetting save = testSettingRepository.save(testSetting);

            result.setAccept(true);
            result.setMessage("Титульный экран успешно создан !");
            result.setData(save.mapToTitleScreenDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData updateName(Long testId, String name) {
        ResponseData result = new ResponseData();

        Test test = testRepository.findById(testId).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + testId);
            return result;
        }

        try {
            test.setName(name);
            Test save = testRepository.save(test);
            result.setAccept(true);
            result.setMessage("Тест успешно обновлен !");
            result.setData(name);
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }
        return result;
    }

    @Override
    public ResponseData findAll() {
        ResponseData result = new ResponseData();

        try {
            List<Test> testList = testRepository.findAll();
            List<TestDto> testDtoList = testList.stream().map(test -> test.mapToDto()).collect(Collectors.toList());

            result.setAccept(true);
            result.setData(testDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }

    @Override
    public ResponseData findAll(TestFiltr testFiltr,int page, int size) {
        ResponseData result = new ResponseData();
        Map<String,Object> map = new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,size, testFiltr.getDesc()?Sort.by("name").descending():Sort.by("name").ascending());
            testFiltr.setName(testFiltr.getName()!=null?testFiltr.getName().toLowerCase():null);
            Page<Test> testPage = testRepository.findAllFiltr(testFiltr,pageable);
            List<TestDto> testDtoList = testPage.getContent()
                    .stream()
                    .map(test -> test.mapToDto()).collect(Collectors.toList());
            map.put("testDtos",testDtoList);
            map.put("totalElements",testPage.getTotalElements());
            map.put("totalPages",testPage.getTotalPages());
            map.put("currentPage",testPage.getNumber());
            result.setAccept(true);
            result.setData(map);
        } catch (Exception e){
            result.setAccept(false);
            result.setMessage("Error get data");
        }
        /*try{
            testFiltr.setName(testFiltr.getName()!=null?testFiltr.getName().toLowerCase():null);
            Comparator<TestDto> comparator = Comparator.comparing(TestDto::getName);
            Comparator<TestDto> reverseComparator = Comparator.comparing(TestDto::getName).reversed();
            List<TestDto> testDtoList = testRepository.findAllFiltr(testFiltr)
                    .map(Test::mapToDto)
                    .sorted(testFiltr.getDesc()?comparator:reverseComparator)
                    .skip(testFiltr.getPage())
                    .limit(testFiltr.getSize())
                    .collect(Collectors.toList());
            result.setAccept(true);
            result.setData(testDtoList);
        } catch (Exception e){
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }*/
        return result;
    }

    @Override
    public ResponseData findAllShort() {
        ResponseData result = new ResponseData();

        try {
            List<BaseDto> findAllShort = testRepository.findAllShort();
            result.setAccept(true);
            result.setData(findAllShort);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error get data");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;

    }

    @Override
    public ResponseData delete(Long id) {
        ResponseData result = new ResponseData();

        try {
            testRepository.deleteById(id);
            result.setAccept(true);
            result.setMessage("Тест успешно удалён !");
            result.setData(id);

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error delete data");
        }
        return result;
    }

    @Override
    public ResponseData findById(Long id) {
        ResponseData result = new ResponseData();

        Test test = testRepository.findById(id).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + id);
        } else {
            result.setAccept(true);
            result.setData(test.mapToDto());
        }
        return result;
    }

    @Override
    public ResponseData findTitleScreenByTestId(Long testId) {
        ResponseData result = new ResponseData();

        Test test = testRepository.findById(testId).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Титульный экран не найден ID = " + testId);
        } else {
            TestSetting testSetting = testSettingRepository.findByTestId(testId).orElse(null);
            result.setAccept(true);
            result.setData(testSetting.mapToTitleScreenDto());
        }
        return result;
    }

    @Override
    public ResponseData updateTitleScreen(TitleScreenDto titleScreenDto) {
        ResponseData result = new ResponseData();
        Test test = testRepository.findById(titleScreenDto.getTestId()).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + titleScreenDto.getTestId());
            return result;
        }

        try {
            TestSetting testSetting = testSettingRepository.findByTestId(titleScreenDto.getTestId()).orElse(null);
            testSetting = titleScreenDto.mapToEntity(testSetting);

            TestSetting save = testSettingRepository.save(testSetting);

            result.setAccept(true);
            result.setMessage("Титульный экран успешно обновлен !");
            result.setData(testSetting.mapToTitleScreenDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData updateSetting(TestSettingDto testSettingDto) {
        ResponseData result = new ResponseData();

        Test test = testRepository.findById(testSettingDto.getTestId()).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + testSettingDto.getTestId());
            return result;
        }

        try {
            TestSetting testSetting = testSettingRepository.findByTestId(testSettingDto.getTestId()).orElse(null);
            testSetting = testSettingDto.mapToEntity(testSetting);

            TestSetting save = testSettingRepository.save(testSetting);

            result.setAccept(true);
            result.setMessage("Тест настройки успешно обновлен !");
            result.setData(testSetting.mapToDto());
        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error saving data");
        }

        return result;
    }

    @Override
    public ResponseData findSettingByTestId(Long testId) {

        ResponseData result = new ResponseData();

        Test test = testRepository.findById(testId).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + testId);
        } else {
            TestSetting testSetting = testSettingRepository.findByTestId(testId).orElse(null);
            result.setAccept(true);
            result.setData(testSetting.mapToDto());
        }
        return result;
    }

    @Override
    public ResponseData updateVersion(Long testId) {
        ResponseData result = new ResponseData();

        Test test = testRepository.findById(testId).orElse(null);

        if (test == null) {
            result.setAccept(false);
            result.setMessage("Тест не найден ID = " + testId);
            return result;
        }

        TestSetting testSetting = testSettingRepository.findByTestId(test.getId()).orElse(null);

        try {
            Test newTest = new Test();
            newTest.setName(test.getName());
            newTest.setVersion((test.getVersion() + 1));
            newTest.setDepartment(test.getDepartment());
            newTest.setParentId(test.getParentId() == null ? test.getId() : test.getParentId());
            newTest.setCreatedBy(test.getCreatedBy());

            Test save = testRepository.save(newTest);
            testRepository.deleteById(testId);

            entityManager.detach(testSetting);

            testSetting.setTest(save);
            testSetting.setId(null);

            testSettingRepository.save(testSetting);


            result.setAccept(true);
            result.setData(save.getId());

        } catch (Exception e) {
            e.printStackTrace();
            result.setAccept(false);
            result.setMessage("Error create new version");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }


}
