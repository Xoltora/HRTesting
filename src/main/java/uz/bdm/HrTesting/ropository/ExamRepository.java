package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.dto.BaseDto;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {


    @Query("SELECT NEW uz.bdm.HrTesting.dto.BaseDto(" +
            "e.test.id," +
            "e.test.name" +
            ") FROM Exam e WHERE e.user.id = :userId AND e.isDeleted = false")
    List<BaseDto> findAllTest(@Param("userId") Long userId);

    @Query("SELECT e FROM Exam e WHERE e.user.id = :userId AND e.isDeleted = false")
    List<Exam> findAllTestByUserId(@Param("userId") Long userId);

    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.user.id = :id and e.test.id = :testId")
    void deleteByUserIdAndTestId(@Param("id") Long id, @Param("testId") Long testId);


    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.user.id = :id")
    void deleteAllByUserId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.user.id = :id AND e.test.id NOT IN (:testIds) AND e.isDeleted = false")
    void deleteAllByUserIdTestIdNot(@Param("id") Long id, @Param("testIds") List<Long> testIds);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Exam e WHERE e.test.id = :testId AND e.isDeleted = false")
    Boolean checkExistsTest(@Param("testId") Long testId);

}
