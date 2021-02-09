package uz.bdm.HrTesting.service;

import uz.bdm.HrTesting.dto.ResponseData;
import uz.bdm.HrTesting.dto.test.TestDto;
import uz.bdm.HrTesting.dto.test.TestSettingDto;
import uz.bdm.HrTesting.dto.test.TitleScreenDto;
import uz.bdm.HrTesting.security.UserPrincipal;

import javax.transaction.Transactional;

public interface TestService {

    @Transactional
    ResponseData save(TestDto testDto, UserPrincipal userPrincipal);

    ResponseData findAll();

    ResponseData findAllShort();

    ResponseData findById(Long id);

    ResponseData delete(Long id);

    ResponseData saveTitleScreen(TitleScreenDto titleScreenDto);

    ResponseData updateTitleScreen(TitleScreenDto titleScreenDto);

    ResponseData updateName(Long testId, String name);

    ResponseData findTitleScreenByTestId(Long testId);

    ResponseData updateSetting(TestSettingDto testSettingDto);

    ResponseData findSettingByTestId(Long testId);

    ResponseData updateVersion(Long testId);

}
