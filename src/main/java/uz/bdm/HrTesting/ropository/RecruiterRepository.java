package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Department;
import uz.bdm.HrTesting.domain.Recruiter;

import java.util.List;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Long> {

    @Override
    @Query(value = "SELECT r from Recruiter r WHERE r.isDeleted = false ORDER BY r.id DESC")
    List<Recruiter> findAll();

    @Modifying
    @Override
    @Query("UPDATE Recruiter r SET r.isDeleted = true WHERE r.id = :id")
    void deleteById(@Param("id") Long id);
}
