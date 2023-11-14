package app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.entities.Department;
import app.repositories.DepartmentRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
public class DepartmentController {

	@Autowired
	private DepartmentRepo deptrepo;

	// 3 Department ADU
	@PostMapping("/admin/adddepartment")
	@Operation(summary = "insert data into departments table", description = "insert a new row into departments table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public Department addDepartment(@Valid @RequestBody Department newDepartment) {

		if (newDepartment.getDepartmentCode() == null) {

			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "department code is must");
		}

		if (deptrepo.findById(newDepartment.getDepartmentCode()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.OK, "department code already exists in the departments table");
		}

		deptrepo.save(newDepartment);

		return newDepartment;
	}

	@DeleteMapping("/admin/deletedepartment/departmentcode-{deptcode}")
	@Operation(summary = "delete from departments table", description = "deletes a row in departments table if dept code is given")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "deleted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public String deleteDepartment(@PathVariable("deptcode") String s) {

		if (s == null || s.isBlank()) {
			return "code is empty";
		}
		if (deptrepo.findById(s).isPresent()) {
			deptrepo.deleteById(s);
			return "deleted in department table";
		} else {
			return "department code doesnot exist in department table";
		}

	}

	@PutMapping("/admin/updatedepartmentname/departmentcode-{deptcode}")
	@Operation(summary = "update data of departments table", description = "updates amount column in row into departments table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "updated data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
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

}
