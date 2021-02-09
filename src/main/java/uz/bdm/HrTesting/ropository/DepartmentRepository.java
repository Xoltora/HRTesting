package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
