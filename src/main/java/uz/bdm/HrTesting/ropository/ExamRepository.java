package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.dto.BaseDto;
import uz.bdm.HrTesting.enums.ExamState;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {


    @Query("SELECT NEW uz.bdm.HrTesting.dto.BaseDto(" +
            "e.test.id," +
            "e.test.name" +
            ") FROM Exam e WHERE e.user.id = :userId AND e.isDeleted = false")
    List<BaseDto> findAllTest(@Param("userId") Long userId);

    @Query("SELECT e FROM Exam e WHERE e.user.id = :userId AND e.state <> :state  AND e.isDeleted = false")
    List<Exam> findAllTestByUserIdStateNot(@Param("userId") Long userId, @Param("state") ExamState state);

    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.user.id = :id and e.test.id = :testId")
    void deleteByUserIdAndTestId(@Param("id") Long id, @Param("testId") Long testId);


    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.user.id = :id")
    void deleteAllByUserId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.user.id = :id AND e.test.id NOT IN (:testIds) AND e.isDeleted = false")
    void deleteAllByUserIdTestIdNot(@Param("id") Long id, @Param("testIds") List<Long> testIds);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Exam e WHERE e.test.id = :testId AND e.isDeleted = false")
    Boolean checkExistsTest(@Param("testId") Long testId);

    @Query("SELECT e FROM Exam e WHERE e.isDeleted = false and e.started is null")
    List<Exam> findAllNotStarted();

    @Query("SELECT e from Exam e WHERE e.isDeleted = false AND e.finished is not null")
    List<Exam> findAllNotChecked();

    @Modifying
    @Override
    @Query("UPDATE Exam e SET e.isDeleted = true where e.id=:id")
    void deleteById(@Param("id") Long id);


    @Query(value = "\n" +
            " with ansvers as (select ed.exam_id as exam_id,  ed.question_id , count(*) as ans_c  from exam_detail ed \t\t\t  \n" +
            "\t\t\t\t  where ed.exam_id = 1 \n" +
            "\t\t\t\t  group by ed.question_id ,ed.exam_id ),\n" +
            "\t\t\t\t  \n" +
            " ques as (select q.id  as question_id, q.count_right_ans, q.answer_type from question q \n" +
            "\t\t RIGHT join test ON test.id = q.test_id\n" +
            "\t\t RIGHT join exam ON exam.test_id = test.id\n" +
            "\t\t  where q.is_deleted is false and exam.id = :examId and test.is_deleted is false ),\n" +
            " \n" +
            "written_count as (select * from question q \n" +
            "\t\t\t\t  right join ques qu on qu.question_id = q.id \n" +
            "\t\t\t\t  right join exam_detail ed on ed.question_id = q.id where q.answer_type = 'WRITTEN'),       \n" +
            "\t  \n" +
            " count_question as (select count(*) from ques),\n" +
            " \n" +
            " marked as (select count(*) from ques q \n" +
            "\t\t\t\tleft join exam_detail ed on q.question_id = ed.question_id \n" +
            "\t\t\t\twhere ed.exam_id = 1 and q.answer_type != 'WRITTEN' \n" +
            "\t\t\t    group by q.question_id ),\n" +
            "\t \t \n" +
            "result as ( select a.exam_id , a.question_id , q.count_right_ans,\n" +
            "\t   count(ed.selectable_ans_id) from ansvers a \n" +
            "\t     left join ques q using(question_id) \n" +
            "  \t     left join exam_detail ed on a.question_id = ed.question_id and a.exam_id = ed.exam_id\n" +
            "    \t left join selectable_answer se ON se.id = ed.selectable_ans_id \n" +
            "       where a.ans_c = q.count_right_ans \n" +
            " \t   and se.right_ans is true \n" +
            "\t GROUP BY a.exam_id , a.question_id , q.count_right_ans\n" +
            " \t having  count(ed.selectable_ans_id) = q.count_right_ans)\n" +
            "\t  \t \t \n" +
            "  \t select * from count_question\n" +
            "  \t  union all\n" +
            "   \t select count(*) from marked\n" +
            " \t  union all\n" +
            " \t select count(*) from result\n" +
            " \t union all\n" +
            "\t select count(*) from written_count\n" +
            "\t  \n" +
            "\t  \n" +
            "\t \n" +
            "    \n" +
            "\n" +
            "\n", nativeQuery = true)
    Object[] getResultExam(@Param("examId") Long examId);
}
