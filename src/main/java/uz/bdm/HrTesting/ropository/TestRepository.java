package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.domain.model.TestFiltr;
import uz.bdm.HrTesting.dto.BaseDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Query("select t from Test t left join t.department d where " +
            "(:#{#testFiltr.name} is null OR lower(t.name) like %:#{#testFiltr.name}%)" +
            " AND ((:#{#testFiltr.department}) is null OR d.id in (:#{#testFiltr.department}))")
    Page<Test> findAllFiltr(@Param("testFiltr") TestFiltr testFiltr,Pageable pageable);
}
