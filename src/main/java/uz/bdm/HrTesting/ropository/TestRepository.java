package uz.bdm.HrTesting.ropository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import uz.bdm.HrTesting.domain.Test;
import uz.bdm.HrTesting.domain.model.TestFiltr;
import uz.bdm.HrTesting.dto.BaseDto;

import java.util.Date;
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
            ") from Test t WHERE t.department.id = :departmentId AND t.isDeleted = false ORDER BY t.id DESC")
    List<BaseDto> findAllShort(@Param("departmentId") Long departmentId);

    @Query("select t from Test t left join t.department d where " +
            "(:#{#testFiltr.name} is null OR lower(t.name) like %:#{#testFiltr.name}%)" +
            " AND ((:#{#testFiltr.department}) is null OR d.id in (:#{#testFiltr.department}))")
    Page<Test> findAllFiltr(@Param("testFiltr") TestFiltr testFiltr,Pageable pageable);

    @Query("select t from Test t left join t.department d where t.isDeleted = false  and ((cast(:fromDate as date) is null and cast(:toDate as date) is null) " +
            "or (cast(t.created as date) >= cast(:fromDate as date) and cast(t.created as date) <= cast(:toDate as date))) and " +
            "(:id is null or d.id in (:id)) ORDER BY t.id DESC")
    Page<Test> findAll(@Param("fromDate") @Temporal Date from,
                       @Param("toDate") @Temporal Date to,
                       @Param("id") List<Long> id,
                       Pageable pageable);
}
