package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.TestSetting;

import java.util.Optional;

@Repository
public interface TestSettingRepository extends JpaRepository<TestSetting,Long> {

    Optional<TestSetting> findByTestId(Long id);
}
