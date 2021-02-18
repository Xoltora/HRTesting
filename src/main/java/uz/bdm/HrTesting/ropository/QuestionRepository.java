package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Question;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Modifying
    @Override
    @Query(value = "UPDATE Question q SET q.isDeleted = true WHERE q.id = :id ")
    void deleteById(@Param("id") Long id);

    @Query(value = "select q from Question q WHERE q.isDeleted = false AND q.test.id = :testId ORDER BY q.id DESC")
    Page<Question> findAllByTestId(@Param("testId") Long testId, Pageable pageable);

    @Query(value = "select q from Question q WHERE q.isDeleted = false AND q.test.id = :testId ORDER BY q.id DESC")
    List<Question> findAllByTestId(@Param("testId") Long testId);

    @Override
    @Query(value = "select q from Question q WHERE q.id = :id and q.isDeleted = false ")
    Optional<Question> findById(@Param("id") Long id);

    @Query("select q from Question q left join q.test t where t.id=(select t.id from Exam e left join e.test et where e.id=:id)")
    List<Question> findByExamId(@Param("id") Long id);
}
