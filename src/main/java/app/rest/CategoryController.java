package app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.entities.Category;
import app.repositories.CategoryRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class CategoryController {

	@Autowired
	private CategoryRepo catrepo;

	// 4 Categories ADU
	@PostMapping("/admin/addcategory")
	@Operation(summary = "insert data into categories table", description = "insert a new row into categories table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public Category addCategory(@Valid @RequestBody Category newCategory) {

		var category = catrepo.findById(newCategory.getCategoryCode());
		if (!category.isPresent()) {
			catrepo.save(newCategory);
			return newCategory;

		} else {
			String message = "Category code already exists. Try another category.";

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}

	}

	@Transactional
	@DeleteMapping("/admin/deletecategory/categorycode-{catcode}")
	@Operation(summary = "delete from categories table", description = "deletes a row in categories table if categorycode is given")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "deleted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public String deleteCategory(@PathVariable("catcode") String s) {

		if (s == null || s.isBlank()) {
			return "code missing";
		}
		if (catrepo.findById(s).isPresent()) {

			catrepo.deleteByCategoryCode(s);

			return "deleted in category table and expenditure table";
		}
		return "category code not found in categories table";

	}

	@PutMapping("/admin/updatecategoryname/categorycode-{catcode}")
	@Operation(summary = "update data of categories table", description = "updates amount column in row into categories table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "updated data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public Category updateCategory(@PathVariable("catcode") String s, @RequestBody Category newCategory) {
		if (s.isBlank()) {
			throw new ResponseStatusException(HttpStatus.OK, "category code is missing");
		}
		var optionalCategory = catrepo.findById(s);
		if (optionalCategory.isPresent()) {

			var category = optionalCategory.get();
			category.setCategoryName(newCategory.getCategoryName());
			catrepo.save(category);

			return category;
		}

		throw new ResponseStatusException(HttpStatus.OK, "code not found in the table");
	}

	// 13
	@CrossOrigin()
	@GetMapping("/listcategories")
	@Operation(summary = "gets all categories in the table", description = "gets all rows from the categories table ")
	public List<Category> allCategories() {

		// if table contains data then list of data else empty list
		var categories = catrepo.findAll();

		return categories;

	}

}
