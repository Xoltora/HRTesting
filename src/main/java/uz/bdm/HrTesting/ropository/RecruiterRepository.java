package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Recruiter;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Long> {
}
