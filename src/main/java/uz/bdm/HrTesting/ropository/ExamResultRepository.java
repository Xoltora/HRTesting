package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.ExamResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult,Long> {

    @Query("SELECT e FROM ExamResult e WHERE e.exam.id = :id AND e.exam.isDeleted = false")
    Optional<ExamResult> findByExamId(@Param("id") Long id);

    @Query(value = "select re.id, re.fio ,count( e.user_id) as users_count, cast ((avg(er.percent)) as float) as percent from exam_result er " +
            "left join exam e on er.exam_id = e.id " +
            "left join user_detail ud using(user_id) " +
            "left join recruiter re on ud.recruiter_id = re.id " +
            "where er.created between :fromDate and :toDate " +
            "group by re.id, re.fio",nativeQuery = true)
    List<Object[]> findByDateReport(@Param("fromDate") @Temporal Date fromDate,
                                    @Param("toDate") @Temporal Date toDate);

    @Query("SELECT er from ExamResult er left join er.exam e on e.id = e.id where e.user.id = :userId and e.test.id = :testId ORDER BY er.id ASC")
    List<ExamResult> findAllByExamId(@Param("testId") Long testId, @Param("userId") Long userId);
}
