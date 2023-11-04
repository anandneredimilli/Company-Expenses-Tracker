package app.rest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.dto.CategoryDto;
import app.dto.ExpenditureDto;
import app.entities.Category;
import app.entities.Department;
import app.entities.Expenditure;
import app.entities.PaymentMode;
import app.repositories.CategoryRepo;
import app.repositories.DepartmentRepo;
import app.repositories.ExpenditureRepo;
import app.repositories.PaymentModeRepo;
import jakarta.transaction.Transactional;

@RestController
public class EntitesController {

	@Autowired
	private ExpenditureRepo exprepo;

	@Autowired
	private CategoryRepo catrepo;

	@Autowired
	private DepartmentRepo deptrepo;

	@Autowired
	private PaymentModeRepo paymentrepo;

//	@GetMapping("/categories")
//	public List<Expenditure> allCategories(){
//		return exprepo.findAll();
//		
//	}

	 //13
	@GetMapping("/listcategories")
	public List<Category> allCategories() {
		
		var categories = catrepo.findAll();
		
			return categories;
	

	}

	
	
	

	// 4 Categories ADU
	@PostMapping("/addcategory")
	public ResponseEntity<?> addCategory(@RequestBody Category newCategory) {
		if (newCategory.getCategoryCode() == null) {
			String message = "Category code cannot be null";
			return ResponseEntity.internalServerError().body(message);
		}
		var category = catrepo.findById(newCategory.getCategoryCode());
		if (!category.isPresent()) {
			catrepo.save(newCategory);
			return ResponseEntity.ok(newCategory);
		} else {
			String message = "Category code already exists. Try another category.";
			return ResponseEntity.badRequest().body(message);
		}

	}
	
	
	

	@Transactional
	@DeleteMapping("/deletecategory/categorycode-{catcode}")
	public ResponseEntity<?> deleteCategory(@PathVariable("catcode") String s) {

		if (catrepo.findById(s).isPresent()) {

			catrepo.deleteByCategoryCode(s);

			return ResponseEntity.ok("deleted in category table and expenditure table");
		}
		return ResponseEntity.ok("category code not found in categories table");

	}

	@PutMapping("/updatecategoryname/categorycode-{catcode}")
	public ResponseEntity<?> updateCategory(@PathVariable("catcode") String s, @RequestBody Category newCategory) {

		var optionalCategory = catrepo.findById(s);
		if (optionalCategory.isPresent()) {

			var category = optionalCategory.get();
			category.setCategoryName(newCategory.getCategoryName());
			catrepo.save(category);
			return ResponseEntity.ok("updated Category name");
		}

		return ResponseEntity.ok("unable to update Category name because category code not found");
	}

	// Department ADU
	@PostMapping("/adddepartment")
	public ResponseEntity<?> addDepartment(@RequestBody Department newDepartment) {

		if (newDepartment.getDepartmentCode() == null) {
			return ResponseEntity.internalServerError().body("department code is must");
		}

		if (deptrepo.findById(newDepartment.getDepartmentCode()).isPresent()) {
			return ResponseEntity.ok("department code already exists in the departments table");
		}

		deptrepo.save(newDepartment);

		return ResponseEntity.ok(newDepartment);
	}

	@Transactional
	@DeleteMapping("/deletedepartment/departmentcode-{deptcode}")
	public ResponseEntity<?> deleteDepartment(@PathVariable("deptcode") String s) {

//		if (!exprepo.findByDepartmentCode(s).isEmpty())
//			exprepo.deleteAllByDepartmentCode(s);

		if (deptrepo.findById(s).isPresent()) {
			deptrepo.deleteById(s);
		} else {
			return ResponseEntity.ok("department code doesnot exist in department table");
		}

		return ResponseEntity.ok("deleted in department table");

	}

	@PutMapping("/updatedepartmentname/departmentcode-{deptcode}")
	public String updateDepartment(@PathVariable("deptcode") String code, @RequestBody Department newDepartment) {
		if (code.isBlank()) {
			return "code missing";
		}
		var optionalDepartment = deptrepo.findById(code);
		if (optionalDepartment.isPresent()) {

			var department = optionalDepartment.get();
			department.setDepartmentName(newDepartment.getDepartmentName());
			deptrepo.save(department);
			return "updated";
		}

		return "code not found in table";
	}

	// PaymentMode ADU
	@PostMapping("/addpaymentmode")
	public ResponseEntity<?> addPaymentMode(@RequestBody PaymentMode newPaymentMode) {

		if (newPaymentMode.getPaymentCode() == null) {
			return ResponseEntity.internalServerError().body("Payment code must not be null");
		}
		if (paymentrepo.findById(newPaymentMode.getPaymentCode()).isPresent()) {
			return ResponseEntity.ok("code already exists in paymentmodes table");
		}

		paymentrepo.save(newPaymentMode);

		return ResponseEntity.ok("added data into paymentmodes table");
	}

	@Transactional
	@DeleteMapping("/deletepaymentmode/paymentcode-{code}")
	public ResponseEntity<?> deletePaymentMode(@PathVariable("code") String code) {

		if (code == null || code.isEmpty()) {
			return ResponseEntity.ok("code is empty");
		}

		if (!paymentrepo.findById(code).isPresent()) {
			return ResponseEntity.internalServerError().body("Payment code not found");
		}

		paymentrepo.deleteById(code);

		return ResponseEntity.ok("deleted in paymentmode table");

	}

	@PutMapping("/updatepaymentmodename/paymentcode-{code}")
	public String updatePaymentModeName(@PathVariable("code") String s, @RequestBody PaymentMode newPaymentMode) {
		var optionalPaymentMode = paymentrepo.findById(s);
		if (optionalPaymentMode.isPresent()) {

			var paymentMode = optionalPaymentMode.get();
			paymentMode.setPaymentName(newPaymentMode.getPaymentName());
			paymentrepo.save(paymentMode);
			return "updated";
		}

		return "Code not found in the table";
	}

	// Expenditure ADU

	@PostMapping("/addexpenditure")
	public ResponseEntity<?> addExpenditure(@RequestBody Expenditure newExpenditure) {
		if (newExpenditure == null) {
			return ResponseEntity.badRequest().body("Expenditure data is required.");
		}

		Expenditure savedExpenditure = exprepo.save(newExpenditure);
		return ResponseEntity.ok(savedExpenditure);
	}

	@DeleteMapping("/deleteexpenditure/expenditureid-{id}")
	public ResponseEntity<?> deleteExpenditure(@PathVariable("id") int id) {
		if (!exprepo.findById(id).isPresent()) {
			return ResponseEntity.ok("no data found with that id");
		}

		exprepo.deleteById(id);
		return ResponseEntity.ok("Expenditure deleted");
	}

	@PutMapping("/update/expenditure/amount/expenditureid-{id}")
	public ResponseEntity<?> updateExpenditureAmount(@PathVariable("id") int id,
			@RequestBody Expenditure newExpenditure) {
		var optionalExpenditure = exprepo.findById(id);
		if (!optionalExpenditure.isPresent()) {
			return ResponseEntity.ok("no data found with that id");
		}

		if (newExpenditure.getAmount() == null) {
			return ResponseEntity.ok("new amount is missing");
		}
		var expenditure = optionalExpenditure.get();

		expenditure.setAmount(newExpenditure.getAmount());

		Expenditure updatedExpenditure = exprepo.save(expenditure);

		return ResponseEntity.ok(updatedExpenditure);

	}

	// 10
	@GetMapping("/expenditure/authorizedby{name}")
	public ResponseEntity<?> getExpendituresAuthorizedBy(@PathVariable("name") String name) {
		if (name == null || name.isEmpty()) {
			return ResponseEntity.badRequest().body("Name parameter is required.");
		} else {
			List<Expenditure> expenditures = exprepo.findAllByauthorizedBy(name);
			return ResponseEntity.ok(expenditures);
		}
	}

	// 11
	@GetMapping("/expenditure/{string}")
	public ResponseEntity<?> getExpensesByDescription(@PathVariable("string") String description) {
		if (description == null || description.isEmpty()) {
			return ResponseEntity.badRequest().body("Description parameter is required.");
		} else {
			List<Expenditure> expenditures = exprepo.findAllByDescriptionContaining(description);
			return ResponseEntity.ok(expenditures);
		}
	}

	// 12
	@GetMapping("/expenditurewithmin-{min}&max-{max}")
	public ResponseEntity<?> getExpensesByMinMaxPrice(@PathVariable("min") Integer minprice,
			@PathVariable("max") Integer maxprice) {

		if (minprice == null || maxprice == null) {
			return ResponseEntity.badRequest().body("Both min and max prices are required.");
		}

		if (minprice < 0 || maxprice < 0) {
			return ResponseEntity.badRequest().body("Min and max prices should be non-negative values.");
		}

		if (minprice > maxprice) {
			return ResponseEntity.badRequest().body("Min price should be less than or equal to max price.");
		}
		List<Expenditure> ti = exprepo.findExpenditureBetweenMinAndMax(minprice, maxprice);
		return ResponseEntity.ok(ti);
	}

	// 13
	@GetMapping("/expenditure/departmentamount")
	public ResponseEntity<?> departmentalExpenditure() {
		List<ExpenditureDto> result = exprepo.groupByDepartmentCode();

		if (result.isEmpty()) {
			return ResponseEntity.ok("expenditure table is empty");
		}
		return ResponseEntity.ok(result);

	}

	// 15
	@GetMapping("/categoryandtotal")
	public ResponseEntity<?> categoryExpenditure() {
		List<CategoryDto> result = exprepo.groupByCategory();

		if (result.isEmpty()) {
			return ResponseEntity.ok("expenditure table is empty");
		}
		return ResponseEntity.ok(result);
	}

	// 9
	@GetMapping("/expenses/{departmentCode}/{startDate}&{endDate}")
	public ResponseEntity<?> getExpensesByStartAndEndDate(@PathVariable("departmentCode") String code,
			@PathVariable("startDate") String startDateString, @PathVariable("endDate") String endDateString) {
		if (exprepo.findByDepartmentCode(code).isEmpty()) {
			return ResponseEntity.ok("no department present in departments tables with that department name");
		}

		LocalDate startDate;
		LocalDate endDate;

		// Validate the startDate
		try {
			startDate = LocalDate.parse(startDateString);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid startDate format. Please use yyyy-MM-dd.");
		}

		// Validate the endDate
		try {
			endDate = LocalDate.parse(endDateString);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid endDate format. Please use yyyy-MM-dd.");
		}

		if (startDate.isAfter(endDate)) {
			return ResponseEntity.badRequest().body("Start date cannot be after end date.");
		}

		return ResponseEntity.ok(exprepo.findExpensesOfADepartmentBetweenTwoDates(code, startDate, endDate));
	}

	// 8
	@GetMapping("/expensessummary/month-{month}")
	public ResponseEntity<?> getSummary(@PathVariable("month") Integer month) {

		if (month < 1 || month > 12) {
			return ResponseEntity.badRequest().body("Month should be between 1 and 12.");
		}
		var result = exprepo.summary(month);
		if (result.isEmpty()) {
			return ResponseEntity.ok("no data found in that month");
		} else {
			return ResponseEntity.ok(result);
		}
	}

	// pagination
	// 5
	@GetMapping("/expenses/category/{catcode}")
	public List<Expenditure> printPage(@PathVariable("catcode") String code) {

		var page = exprepo.findByCategoryCode(code, PageRequest.of(0, 5, Sort.by("Id").ascending()));

		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return list;

	}

	// 6
	@GetMapping("/expenses/paymentmode/{code}")
	public List<Expenditure> printPage1(@PathVariable("code") String code) {
		// Retrieve given page with 5 rows in the page
		var page = exprepo.findByPaymentMode(code, PageRequest.of(0, 5, Sort.by("id").ascending()));

		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return list;

	}

	// 7
	@GetMapping("/pagination/expenses/{startDate}&{endDate}")
	public ResponseEntity<?> getExpensesByStartAndEndDate1(@PathVariable("startDate") String startDateString,
			@PathVariable("endDate") String endDateString) {

		LocalDate startDate;
		LocalDate endDate;

		// Validate the startDate
		try {
			startDate = LocalDate.parse(startDateString);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid startDate format. Please use yyyy-MM-dd.");
		}

		// Validate the endDate
		try {
			endDate = LocalDate.parse(endDateString);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid endDate format. Please use yyyy-MM-dd.");
		}

		if (startDate.isAfter(endDate)) {
			return ResponseEntity.badRequest().body("Start date cannot be after end date.");
		}

		var page = exprepo.findAll(PageRequest.of(0, 5, Sort.by("expenditureDate").ascending()));

		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return ResponseEntity.ok(list);

	}

}
