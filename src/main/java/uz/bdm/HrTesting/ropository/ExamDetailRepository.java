package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.bdm.HrTesting.domain.ExamDetail;
import uz.bdm.HrTesting.domain.SelectableAnswer;

import java.util.List;
import java.util.Optional;

@RequestMapping
public interface ExamDetailRepository extends JpaRepository<ExamDetail, Long> {

    List<ExamDetail> findByExamIdAndIsDeletedNot(Long id, Boolean isDeleted);

    Optional<ExamDetail> findByExamIdAndQuestionIdAndIsDeletedNot(Long id, Long questionId, Boolean isDeleted);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ExamDetail e WHERE e.exam.id = :examId AND e.question.id = :questionId AND e.selectableAnswer.id = :selectableId AND e.isDeleted = false")
    Boolean checkExistAnswer(@Param("examId") Long examId, @Param("questionId") Long questionId, @Param("selectableId") Long selectableId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ExamDetail e WHERE e.exam.id = :examId AND e.question.id = :questionId  AND e.isDeleted = false")
    Boolean checkExistAnswerWritten(@Param("examId") Long examId, @Param("questionId") Long questionId);

    @Modifying
    @Query("UPDATE ExamDetail e SET e.isDeleted = TRUE WHERE e.exam.id = :examId AND e.question.id = :questionId AND e.selectableAnswer.id = :selectableId")
    void deleteByExamIdAndQuestionIdAndSelectableId(@Param("examId") Long examId, @Param("questionId") Long questionId, @Param("selectableId") Long selectableId);

    @Modifying
    @Query("UPDATE ExamDetail e SET e.isDeleted = TRUE WHERE e.exam.id = :examId AND e.question.id = :questionId ")
    void deleteByExamIdAndQuestionId(@Param("examId") Long examId, @Param("questionId") Long questionId);

    @Query("SELECT e FROM ExamDetail e WHERE e.exam.id= :examId AND e.question.answerType = 'WRITTEN' AND e.right is null AND e.isDeleted = FALSE ")
    List<ExamDetail> findAllForCheckQuestion(@Param("examId") Long examId);

}
