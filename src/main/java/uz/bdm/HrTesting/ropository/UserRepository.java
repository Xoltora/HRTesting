package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByLogin(String login);

  @Query(value = "select u from u_user u WHERE u.isDeleted = false and u.id <> :id ORDER BY  u.id DESC")
  Page<User> findAllNotId(@Param("id") Long id, Pageable pageable);


  @Override
  @Query("UPDATE u_user u SET u.isDeleted = true WHERE u.id = 1")
  void deleteById(Long aLong);
}
