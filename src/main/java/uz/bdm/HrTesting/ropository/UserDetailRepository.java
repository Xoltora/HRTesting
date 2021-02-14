package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.User;
import uz.bdm.HrTesting.domain.UserDetail;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUserId(Long id);

    @Override
    @Query(value = "select u from UserDetail u WHERE u.user.isDeleted = false ORDER BY u.id DESC")
    List<UserDetail> findAll();

    @Query("select u from UserDetail u left join u.user us left join u.department d left join u.recruiter r where" +
            " us.isDeleted = false and (:departmentId is null or d.id=:departmentId) and (:recruiterId is null or r.id=:recruiterId)")
    Page<UserDetail> findAll(@Param("departmentId") Long departmentId,
                             @Param("recruiterId") Long recruiterId,
                             Pageable pageable);
}
