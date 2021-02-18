package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.dto.BaseDto;
import uz.bdm.HrTesting.enums.ExamState;

import java.util.Date;
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


    @Query(value = "" +
            " with ansvers as (select ed.exam_id as exam_id,  ed.question_id , count(*) as ans_c  from exam_detail ed   " +
            "  where ed.exam_id = :examId " +
            "  group by ed.question_id ,ed.exam_id )," +
            "  " +
            " ques as (select q.id  as question_id, q.count_right_ans, q.answer_type from question q " +
            " RIGHT join test ON test.id = q.test_id" +
            " RIGHT join exam ON exam.test_id = test.id" +
            "  where q.is_deleted is false and exam.id = :examId and test.is_deleted is false )," +
            " " +
            "written_count as (select * from question q " +
            "  right join ques qu on qu.question_id = q.id " +
            "  right join exam_detail ed on ed.question_id = q.id where q.answer_type = 'WRITTEN'),       " +
            "  " +
            " count_question as (select count(*) from ques)," +
            " " +
            " marked as (select count(*) from ques q " +
            "left join exam_detail ed on q.question_id = ed.question_id " +
            "where ed.exam_id = :examId and q.answer_type != 'WRITTEN' " +
            "    group by q.question_id )," +
            "  " +
            "result as ( select a.exam_id , a.question_id , q.count_right_ans," +
            "   count(ed.selectable_ans_id) from ansvers a " +
            "     left join ques q using(question_id) " +
            "       left join exam_detail ed on a.question_id = ed.question_id and a.exam_id = ed.exam_id" +
            "     left join selectable_answer se ON se.id = ed.selectable_ans_id " +
            "       where a.ans_c = q.count_right_ans " +
            "    and se.right_ans is true " +
            " GROUP BY a.exam_id , a.question_id , q.count_right_ans" +
            "  having  count(ed.selectable_ans_id) = q.count_right_ans)" +
            "    " +
            "   select * from count_question" +
            "    union all" +
            "    select count(*) from marked" +
            "   union all" +
            "  select count(*) from result" +
            "  union all" +
            " select count(*) from written_count", nativeQuery = true)
    Object[] getResultExam(@Param("examId") Long examId);

    @Query("select e from Exam e left join e.test t left join t.department d where e.state=:state and (:id is null or d.id=:id) and " +
            "((cast(:fromDate as date) is null or cast(:toDate as date) is null) or (cast(:fromDate as date)<e.created " +
            "and cast(:toDate as date)>e.created))")
    Page<Exam> findByState(@Param("state") ExamState examState,
                           @Param("id") Long id,
                           @Param("fromDate") @Temporal Date fromDate,
                           @Param("toDate") @Temporal Date toDate,
                           Pageable pageable);



    @Query(value = "with ansvers as (select ed.exam_id as exam_id,  ed.question_id , count(*) as ans_c  from exam_detail ed     " +
            "                              where ed.exam_id = :examId   " +
            "                              group by ed.question_id ,ed.exam_id ),  " +
            "    ansversDetail as (select ed.exam_id ,  ed.question_id , ed.selectable_ans_id  from exam_detail ed     " +
            "                              where ed.exam_id = :examId" +
            "          and ed.is_deleted is false  " +
            "                              group by ed.question_id ,ed.exam_id , ed.selectable_ans_id ),  " +
            "     ques as (select q.id  as question_id, q.count_right_ans, q.answer_type, q.text , q.image_name, q.image_path  from question q   " +
            "                             RIGHT join test ON test.id = q.test_id  " +
            "                             RIGHT join exam ON exam.test_id = test.id  " +
            "                             where q.is_deleted is false and exam.id = :examId" +
            "                 and test.is_deleted is false ),            " +
            "     written as (select qu.question_id ,ed.written_ans_text as text, ed.right_ans as is_right  from question q   " +
            "                             right join ques qu on qu.question_id = q.id   " +
            "                             right join exam_detail ed on ed.question_id = q.id  " +
            "                 where q.answer_type = 'WRITTEN' and ed.is_deleted is false ),      " +
            "    selectable as (select se.id, se.question_id, se.text, se.right_ans ,    " +
            "                 case when ad.selectable_ans_id is null then false else true end as isMarked  from selectable_answer se  " +
            "                 left join ansversDetail ad on se.id = ad.selectable_ans_id    " +
            "                 where se.is_deleted is false),  " +
            "    count_question as (select count(*) from ques),  " +
            "    marked as (select count(*) from ques q   " +
            "                  left join exam_detail ed on q.question_id = ed.question_id   " +
            "                              where ed.exam_id = :examId " +
            "                  and q.answer_type != 'WRITTEN'   " +
            "                              group by q.question_id ),  " +
            "   questions  as (SELECT  q.question_id, q.count_right_ans , q.answer_type , q.text ,q.image_name, q.image_path,   " +
            "                   case when se is null then null else row_to_json(se) end as see FROM ques q   " +
            "                   left join selectable se using(question_id)   " +
            "                   group by  q.question_id, q.count_right_ans , q.answer_type ,se, q.text ,q.image_name, q.image_path ),  " +
            "   questionsWithVariants as (select q.question_id, q.count_right_ans, q.answer_type, q.text,q.image_name, q.image_path, JSON_AGG(q.see) as arr   " +
            "                from questions q   " +
            "                group by  q.question_id, q.count_right_ans , q.answer_type , q.text, q.image_name, q.image_path),  " +
            "   result as ( select a.exam_id , a.question_id,    " +
            "                   q.count_right_ans, count(ed.selectable_ans_id)   " +
            "                   from ansvers a   " +
            "                                  left join ques q using(question_id)   " +
            "                                  left join exam_detail ed using(question_id, exam_id)  " +
            "                                  left join selectable_answer se ON se.id = ed.selectable_ans_id   " +
            "                                    where a.ans_c = q.count_right_ans   " +
            "                               and se.right_ans is true   " +
            "                     GROUP BY a.exam_id , a.question_id , q.count_right_ans   " +
            "                               having  count(ed.selectable_ans_id) = q.count_right_ans)  " +
            "        select q.question_id , q.answer_type ,  " +
            "                          case when q.answer_type = 'WRITTEN' then w.text else cast(q.arr as text) end ,   " +
            "                           q.text,q.image_name, q.image_path , " +
            "                      case when q.answer_type = 'WRITTEN' and w.is_right is not null then w.is_right  " +
            "                           when w.is_right is null then false  " +
            "                           when r.exam_id is not null then true else false end as isTrue  " +
            "                          from questionsWithVariants q   " +
            "                           left join result r using(question_id)  " +
            "                           left join written w using(question_id)" ,nativeQuery = true)
    List<Object[]> findReportByExamId(@Param("examId") Long examId);
}
