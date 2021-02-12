package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.bdm.HrTesting.domain.ExamDetail;
import uz.bdm.HrTesting.domain.SelectableAnswer;

import java.util.List;

@RequestMapping
public interface ExamDetailRepository extends JpaRepository<ExamDetail, Long> {

    List<ExamDetail> findByExamIdAndIsDeletedNot(Long id, Boolean isDeleted);

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

    @Query("select e.selectableAnswer from ExamDetail e left join e.question q where q.id=:id")
    List<SelectableAnswer> getSelectableAnswerByQuestionId(@Param("id") Long id);

    @Query("select e.writtenAnswerText from ExamDetail e left join e.question q where q.id=:id")
    String getWrittenTextByQuestionId(@Param("id") Long id);

    @Query("select count(e) from ExamDetail e left join e.question q left join e.selectableAnswer s " +
            "where q.id=:id and s.right=true group by q")
    Integer checkAnswer(@Param("id") Long id);


}
