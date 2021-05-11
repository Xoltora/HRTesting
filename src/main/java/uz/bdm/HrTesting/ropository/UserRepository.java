package uz.bdm.HrTesting.ropository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Role;
import uz.bdm.HrTesting.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByLogin(String login);

  @Query(value = " select * from u_user u where id not in   " +
          "(select user_id from user_authority   " +
          " where authority_name = 'ROLE_CANDIDATE')  " +
          "and u.is_deleted = false and u.id <> :id  ORDER BY  u.id DESC" , nativeQuery = true)
  Page<User> findAllNotId(@Param("id") Long id, Pageable pageable);

  @Query(value = " select * from u_user u where id in   " +
          "(select user_id from user_authority   " +
          " where authority_name = 'ROLE_RECRUITER')  " +
          "and u.is_deleted = false ORDER BY u.id DESC" ,nativeQuery = true) ////  and u.id <> :id
  List<User> findAllRecruiter(@Param("id") Long id);

  @Modifying
  @Override
  @Query("UPDATE u_user u SET u.isDeleted = true WHERE u.id = :id")
  void deleteById(@Param("id") Long id);
}
