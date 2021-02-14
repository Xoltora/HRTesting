package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.ExamResult;

import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult,Long> {

    @Query("SELECT e FROM ExamResult e WHERE e.exam.id = :id AND e.exam.isDeleted = false")
    Optional<ExamResult> findByExamId(@Param("id") Long id);
}
