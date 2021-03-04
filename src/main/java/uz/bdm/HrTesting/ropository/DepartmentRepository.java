package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Department;
import uz.bdm.HrTesting.domain.Test;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    @Override
    @Query(value = "SELECT d from Department d WHERE d.isDeleted = false ORDER BY d.id DESC")
    List<Department> findAll();

    @Modifying
    @Override
    @Query("UPDATE Department d SET d.isDeleted = true WHERE d.id = :id")
    void deleteById(@Param("id") Long id);
}
