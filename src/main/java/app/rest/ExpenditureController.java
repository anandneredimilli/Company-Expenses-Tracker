package app.rest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import app.entities.Expenditure;
import app.repositories.ExpenditureRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class ExpenditureController {

	@Autowired
	private ExpenditureRepo exprepo;

	// 1 Expenditure ADU
	// 1.1
	@PostMapping("/admin/addexpenditure")
	@Operation(summary = "insert data into expenditures table", description = "insert a new row into expenditures table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public Expenditure addExpenditure(@RequestBody Expenditure newExpenditure) {

		Expenditure savedExpenditure = exprepo.save(newExpenditure);
		return savedExpenditure;

	}

	// 1.2
	@PutMapping("/admin/deleteexpenditure/expenditureid-{id}")
	@Operation(summary = "delete from expenditures table", description = "deletes a row in expenditures table if id is given")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "deleted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public String deleteExpenditure(@PathVariable("id") String id) {

		if (id == null || id.isBlank()) {
			return "id cannot be null or blank";
		}
		Integer expenditureId = null;
		try {
			expenditureId = Integer.parseInt(id);
		} catch (NumberFormatException ex) {
			return "id must be number";
		}
		if (!exprepo.findById(expenditureId).isPresent()) {
			return "no data found with that id";
		}

		exprepo.deleteById(expenditureId);
		return "Expenditure deleted";
	}

	// 1.3
	@PutMapping("/admin/update/expenditure/amount/expenditureid-{id}")
	@Operation(summary = "update data of expenditures table", description = "updates amount column in row into expenditures table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetched data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public Expenditure updateExpenditureAmount(@PathVariable("id") String id, @RequestBody Expenditure newExpenditure) {
		Integer expenditureId = null;
		try {
			expenditureId = Integer.parseInt(id);
		} catch (NumberFormatException ex) {
			ex.getMessage();
		}
		Optional<Expenditure> optionalExpenditure = exprepo.findById(expenditureId);
		if (!optionalExpenditure.isPresent()) {
			throw new ResponseStatusException(HttpStatus.OK, "no data found with that id");
		}

		if (newExpenditure.getAmount() == null) {
			throw new ResponseStatusException(HttpStatus.OK, "new amount value is missing");
		}

		if (newExpenditure.getAmount() < 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"constraint violation, amount cannot be negative");
		}
		var expenditure = optionalExpenditure.get();

		expenditure.setAmount(newExpenditure.getAmount());

		Expenditure updatedExpenditure = exprepo.save(expenditure);

		return updatedExpenditure;

	}

	// pagination
	// 5
	@GetMapping("/expenses/category/{catcode}")
	@Operation(summary = "expenses by category (pagination)", description = "list expenses by category and sort by id")
	public List<Expenditure> printPage(@PathVariable("catcode") String code) {

		var page = exprepo.findByCategoryCode(code, PageRequest.of(0, 5, Sort.by("id").ascending()));

		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return list;

	}

	// 6
	@GetMapping("/expenses/paymentmode/{code}")
	@Operation(summary = "expenses by payment mode (pagination)", description = "list expenses by payment mode and sort by id")
	public List<Expenditure> printPage1(@PathVariable("code") String code) {

		var page = exprepo.findByPaymentMode(code, PageRequest.of(0, 5, Sort.by("id").ascending()));

		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return list;

	}

	// 7
	@GetMapping("/pagination/expenses/{startDate}&{endDate}")
	@Operation(summary = "expenses between two given dates (pagination)", description = "list expenses between two dates and sort by date in descending order")
	public List<Expenditure> getExpensesByStartAndEndDate1(@PathVariable("startDate") String startDateString,
			@PathVariable("endDate") String endDateString) {

		LocalDate startDate;
		LocalDate endDate;

		try {
			startDate = LocalDate.parse(startDateString);
			endDate = LocalDate.parse(endDateString);
		} catch (DateTimeParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid startDate or endDate format. Please use yyyy-MM-dd.");
		}

		if (startDate.isAfter(endDate)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date.");
		}

		var page = exprepo.findAll(PageRequest.of(0, 6, Sort.by("expenditureDate").ascending()));

		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			if (p.getExpenditureDate() == null) {
				continue;
			}
			list.add(p);
		}
		return list;

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

	// 9
	@GetMapping("/expenses/{departmentCode}/{startDate}&{endDate}")
	@Operation(summary = "expenditures of a departemnt between two given dates", description = "list all expenses of a departemnt between two given dates")
	public List<Expenditure> getExpensesByStartAndEndDate(@PathVariable("departmentCode") String code,
			@PathVariable("startDate") String startDateString, @PathVariable("endDate") String endDateString) {

		if (exprepo.findByDepartmentCode(code).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"no department present in departments tables with that department name");
		}

		LocalDate startDate;
		LocalDate endDate;

		try {
			startDate = LocalDate.parse(startDateString);
			endDate = LocalDate.parse(endDateString);
		} catch (DateTimeParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid startDate or endDate format. Please use yyyy-MM-dd.");
		}

		if (startDate.isAfter(endDate)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date.");
		}

		return exprepo.findExpensesOfADepartmentBetweenTwoDates(code, startDate, endDate);
	}

	// 10 list all expenses authorized by given employee name
	@GetMapping("/expenditure/authorizedby-{name}")
	@Operation(summary = "expenditures authorized by given employee", description = "list all expenses authorized by given employee name")
	public List<Expenditure> getExpendituresAuthorizedBy(
			@Parameter(description = "name") @PathVariable("name") String name) {

		if (name == null || name.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description parameter is required");
		}

		List<Expenditure> expenditures = exprepo.findAllByauthorizedBy(name);

		return expenditures;

	}

	// 11 list all expenses where description contains the given string
	@GetMapping("/expenditure/description-contains-{string}")
	@Operation(summary = "expenditures with description containing given string", description = "list all expenses where description contains the given string")
	public List<Expenditure> getExpensesByDescription(
			@Parameter(description = "sub string") @PathVariable("string") String string) {

		if (string == null || string.isEmpty()) {

			throw new ResponseStatusException(HttpStatus.OK, "Description parameter is required");

		}

		List<Expenditure> expenditures = exprepo.findAllByDescriptionContaining(string);

		return expenditures;

	}

	// 12 list all expenses where amount is b/w min and max values
	@GetMapping("/expenditurewithmin-{min}&max-{max}")
	@Operation(summary = "expenses b/w min & max amount", description = "list all expenses where amount is b/w min and max values")
	public Object getExpensesByMinMaxPrice(@PathVariable("min") String minprice, @PathVariable("max") String maxprice) {

		int min = 0;
		int max = 0;

		try {
			try {
				min = Integer.parseInt(minprice);
				max = Integer.parseInt(maxprice);
			} catch (NumberFormatException ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minimum and maximum amount must be numbers");

			}
			if (min < 0 || max < 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Min and max prices should be non-negative values.");
			}

			if (min > max) {

				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Min price should be less than or equal to max price.");
			}

			List<Expenditure> result = exprepo.findExpenditureBetweenMinAndMax(min, max);

			return result;
		} catch (ResponseStatusException ex) {
			return ex.getMessage();
		}
	}

	// 14 list department and total amount spend in the department
	@GetMapping("/expenditure/departmentamount")
	@Operation(summary = "departments - totalamount", description = "list department and total amount spend in the department")
	public List<ExpenditureDto> departmentalExpenditure() {

		// if table contains data then list of data else empty list
		List<ExpenditureDto> result = exprepo.groupByDepartmentCode();

		return result;
	}

	// 15 list all categories and total amount spend in the category
	@GetMapping("/categoryandtotal")
	@Operation(summary = "categories - totalamount", description = "list all categories and total amount spend in the category")
	public List<CategoryDto> categoryExpenditure() {

		// if table contains data then list of data else empty list
		List<CategoryDto> result = exprepo.groupByCategory();

		return result;
	}

}
