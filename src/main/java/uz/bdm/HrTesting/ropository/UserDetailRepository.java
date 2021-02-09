package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
