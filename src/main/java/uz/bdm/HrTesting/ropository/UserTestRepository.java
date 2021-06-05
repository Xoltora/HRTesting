package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.domain.UserTest;

import java.util.Date;
import java.util.List;

@Repository
public interface UserTestRepository extends JpaRepository<UserTest, Long> {

    UserTest findByTestAndUser(Test test, User user);

    @Query("SELECT ut from UserTest ut WHERE ut.user.id = :id AND ut.test.id NOT IN (SELECT e.test.id from Exam e WHERE e.state = 'ON_PROCESS' AND e.user.id = ut.user.id)")
    List<UserTest> findAllByStateNotStarted(@Param("id") Long id);

    @Query("SELECT ut FROM UserTest ut LEFT JOIN ut.test t LEFT JOIN t.department d WHERE t.isDeleted = false AND ut.numberOfAttempts - ut.completedAttempts <> 0 AND (:id is null or d.id=:id) AND " +
            "((cast(:fromDate as date) is null or cast(:toDate as date) is null) or (cast(:fromDate as date) <= ut.created" +
            " AND cast(:toDate as date) >= ut.created)) and (:fio is null OR lower(ut.user.fio) like lower(concat('%',cast(:fio as text),'%')))  ORDER BY ut.id DESC")
    Page<UserTest> findByNotStarted(@Param("id") Long id,
                                    @Param("fio") String fio,
                                    @Param("fromDate") @Temporal Date from,
                                    @Param("toDate") @Temporal Date to,
                                    Pageable pageable);


    @Query("select ut from UserTest ut where ut.test.id not in (SELECT e.test.id from Exam e WHERE e.state = 'ON_PROCESS' and e.user.id = ut.user.id)")
    Page<UserTest> findAllByStateNotProcess(Pageable pageable);

//    @Query(value = "with t as (select CAST(u.created as DATE) as cr, d.name as nam, count(*) as coun from u_user u " +
//            " left join user_authority ua on ua.user_id = u.id " +
//            " left join user_detail ut on ut.user_id = u.id " +
//            " left join department d on d.id = ut.department_id " +
//            " where ua.authority_name = 'ROLE_CANDIDATE' and u.created > CAST((CAST(NOW() as DATE) - INTERVAL '7' day) as DATE)" +
//            " group by u.created, d.name) " +
//            " select cr, nam, sum(coun) from t group by cr, nam",
//            nativeQuery = true)
//    List<Object[]> findByWeekAgo();

    @Query(value = "with" +
            "            months as(" +
            "            select TO_CHAR(cast(row as date), 'month') as month  from generate_series(" +
            "             cast(date_trunc('year', now()) as date)," +
            "            current_date ," +
            "            interval '1 month') row" +
            "            )," +
            "            dates as (SELECT EXTRACT(MONTH FROM TO_DATE(m.month, 'Month')) AS \"number\", m.month from months m)," +
            "            deprtmentWithDates as (" +
            "            select d2.id , d2.name, d.number, d.month  from dates d, department d2 " +
            "            )," +
            "            rr as (  " +
            "            select dt.id , dt.name, dt.month, dt.number, count(ud.created) filter(where ud.created notnull)" +
            "            from deprtmentwithdates dt " +
            "            left join user_detail ud on ud.department_id = dt.id and dt.month = TO_CHAR(cast(ud.created as date), 'month') " +
            "            group by 1, 2, 3, 4" +
            "            order by 1, 4 " +
            "            )" +
            "               select rr.name, rr.month, rr.count from rr",
            nativeQuery = true)
    List<Object[]> findByYear();

    @Query(value = "with" +
            "   dates as(" +
            "     select cast(row as date) as date  from generate_series(" +
            "           current_date -6 ," +
            "           current_date ," +
            "           interval '1 day'" +
            "         ) row" +
            "   )," +
            "   deprtmentWithDates as (" +
            "      select d2.id , d2.name, d.date  from dates d, department d2 " +
            "   )," +
            "   rr as (  " +
            "      select dt.id , dt.name , dt.date , count(ud.created) filter(where ud.created notnull)" +
            "      from deprtmentwithdates dt " +
            "      left join user_detail ud on ud.department_id = dt.id and cast(ud.created as date) = dt.date" +
            "      group by 1 , 2, 3" +
            "      order by 1 " +
            "   )" +
            "   select * from rr",
            nativeQuery = true)
    List<Object[]> findByWeekAgo();
}
