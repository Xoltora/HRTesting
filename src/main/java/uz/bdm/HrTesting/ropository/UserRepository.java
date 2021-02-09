package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByLogin(String login);

//  @Override
//  @Query(value = "select u from User u WHERE u.isDeleted = false ORDER BY u.id DESC")
//  List<User> findAll();


//  @Override
//  @Query("UPDATE u_user SET u.isDeleted = true WHERE u.id = 1")
//  void deleteById(Long aLong);
}
