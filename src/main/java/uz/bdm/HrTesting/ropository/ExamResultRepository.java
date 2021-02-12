package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.ExamResult;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult,Long> {
}
