package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.bdm.HrTesting.domain.SelectableAnswer;

import java.util.List;
import java.util.Optional;

public interface SelectableAnswerRepository extends JpaRepository<SelectableAnswer, Long> {
    List<SelectableAnswer> findByQuestionIdAndIsDeletedNot(Long id, Boolean isDeleted);

    @Modifying
    @Query(value = "UPDATE SelectableAnswer s SET s.isDeleted = true WHERE s.question.id = :id")
    void deleteAllByQuestionId(@Param("id") Long id);

    @Modifying
    @Override
    @Query(value = "UPDATE SelectableAnswer s SET s.isDeleted = true WHERE s.id = :id ")
    void deleteById(@Param("id") Long id);


    @Modifying
    @Query(value = "UPDATE SelectableAnswer s SET s.isDeleted = true WHERE s.id NOT IN (:ids) and s.isDeleted = false")
    void deleteByIdNot(@Param("ids") List<Long> ids);



    List<SelectableAnswer> findByQuestionIdAndRight(Long id, Boolean right);
}
