package uz.bdm.HrTesting.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.dto.BaseDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test,Long> {

    @Modifying
    @Override
    @Query(value = "UPDATE Test t SET t.isDeleted = true WHERE t.id = :id")
    void deleteById(@Param("id") Long id);

    @Override
    @Query(value = "select t from Test t WHERE t.isDeleted = false ORDER BY t.id DESC")
    List<Test> findAll();

    @Override
    @Query(value = "select t from Test t WHERE t.id = :id and t.isDeleted = false ")
    Optional<Test> findById(@Param("id") Long id);

    @Query(value = "select new uz.bdm.HrTesting.dto.BaseDto(" +
            "t.id," +
            "t.name" +
            ") from Test t WHERE t.isDeleted = false ORDER BY t.id DESC")
    List<BaseDto> findAllShort();
}
