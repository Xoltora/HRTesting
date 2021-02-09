package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.WrittenAnswer;

import java.util.List;
import java.util.Optional;

@Repository
public interface WrittenAnswerRepository extends JpaRepository<WrittenAnswer, Long> {

    @Modifying
    @Query(value = "UPDATE WrittenAnswer w SET w.isDeleted = true WHERE w.question.id = :id")
    void deleteAllByQuestionId(@Param("id") Long id);

    List<WrittenAnswer> findByQuestionId(Long id);
}
