package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Exam;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.domain.UserTest;

import java.util.List;

@Repository
public interface UserTestRepository extends JpaRepository<UserTest, Long> {

    UserTest findByTestAndUser(Test test, User user);

    @Query("SELECT ut from UserTest ut WHERE ut.user.id = :id AND ut.test.id NOT IN (SELECT e.test.id from Exam e WHERE e.state = 'ON_PROCESS')\n" +
            "AND ut.user.id NOT IN (SELECT e.user.id from Exam e WHERE e.state = 'ON_PROCESS')")
    List<UserTest> findAllByStateNotStarted(@Param("id") Long id);


//    @Query("select ut from UserTest ut where ut.test.id not in (SELECT e.test.id from Exam e WHERE e.state <> 'ON_PROCESS') and ut.user.id = :id")
//    List<UserTest> findAllByStateNotProcess(@Param("id") Long id);
}
