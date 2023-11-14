package app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.dto.CategoryDto;
import app.dto.ExpenditureDto;
import app.entities.Expenditure;

public interface ExpenditureRepo extends JpaRepository<Expenditure, Integer> {

	void deleteAllByCategoryCode(String s);

	void deleteAllByDepartmentCode(String s);

	void deleteAllByPaymentMode(String code);

	List<Expenditure> findByCategoryCode(String code, PageRequest pageRequest);

	List<Expenditure> findByDepartmentCode(String code);

	List<Expenditure> findByPaymentMode(String code, PageRequest pageRequest);

	// 8
	@Query("select e.categoryCode as categoryCode,e.category.categoryName as categoryName ,sum(e.amount) totalAmount from Expenditure e where month(e.expenditureDate)=:month group by e.categoryCode,e.category.categoryName")
	List<CategoryDto> summary(@Param("month") int month);

	// 9
	@Query("from Expenditure e where e.departmentCode=:departmentCode and ( e.expenditureDate between :startDate and :endDate)  ")
	List<Expenditure> findExpensesOfADepartmentBetweenTwoDates(@Param("departmentCode") String code,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	// 10
	List<Expenditure> findAllByauthorizedBy(String name);

	// 11
	List<Expenditure> findAllByDescriptionContaining(String s);

	// 12
	@Query("from Expenditure e where e.amount>=:min and e.amount<=:max")
	List<Expenditure> findExpenditureBetweenMinAndMax(@Param("min") double min, @Param("max") double max);

	// 14
	@Query("select e.department.departmentName as departmentName,e.departmentCode as departmentCode,sum(e.amount) as totalAmount from Expenditure e group by e.department.departmentName,e.departmentCode")
	List<ExpenditureDto> groupByDepartmentCode();

	// 15
	@Query("select e.category.categoryName as categoryName,e.categoryCode as categoryCode,sum(e.amount) as totalAmount from Expenditure e group by e.category.categoryName,e.categoryCode")
	List<CategoryDto> groupByCategory();
}
