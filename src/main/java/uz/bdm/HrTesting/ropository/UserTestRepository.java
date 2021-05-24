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

    @Query(value = "with t as (select CAST(u.created as DATE) as cr, d.name as nam, count(*) as coun from u_user u left join user_authority ua on ua.user_id = u.id " +
            " left join user_detail ut on ut.user_id = u.id left join department d on d.id = ut.department_id " +
            " where ua.authority_name = 'ROLE_CANDIDATE' and u.created > CAST((CAST(NOW() as DATE) - INTERVAL '7' day) as DATE)" +
            " group by u.created, d.name) " +
            " select cr, nam, sum(coun) from t group by cr, nam",
            nativeQuery = true)
    Object[] findByWeekAgo();
}
