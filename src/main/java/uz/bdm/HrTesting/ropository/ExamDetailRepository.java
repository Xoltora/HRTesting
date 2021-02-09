package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.bdm.HrTesting.domain.ExamDetail;

import java.util.List;

@RequestMapping
public interface ExamDetailRepository extends JpaRepository<ExamDetail, Long> {

    List<ExamDetail> findByExamIdAndIsDeletedNot(Long id, Boolean isDeleted);
}
