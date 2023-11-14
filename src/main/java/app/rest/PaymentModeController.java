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

import app.entities.PaymentMode;
import app.repositories.PaymentModeRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
public class PaymentModeController {

	@Autowired
	private PaymentModeRepo paymentrepo;

	// 2 PaymentMode ADU
	@PostMapping("/admin/addpaymentmode")
	@Operation(summary = "insert data into paymentmodes table", description = "insert a new row into paymentmodes table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public PaymentMode addPaymentMode(@Valid @RequestBody PaymentMode newPaymentMode) {

		if (paymentrepo.findById(newPaymentMode.getPaymentCode()).isPresent()) {

			throw new ResponseStatusException(HttpStatus.OK, "code already exists in paymentmodes table");
		}

		paymentrepo.save(newPaymentMode);

		return newPaymentMode;
	}

	@DeleteMapping("/admin/deletepaymentmode/paymentcode-{code}")
	@Operation(summary = "delete from paymentmodes table", description = "deletes a row in paymentmodes table if payment code is given")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetched data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public String deletePaymentMode(@PathVariable("code") String code) {

		if (code == null || code.isBlank()) {
			return "code is empty";
		}

		if (!paymentrepo.findById(code).isPresent()) {
			return "Payment code not found";
		}

		paymentrepo.deleteById(code);

		return "deleted in paymentmode table";

	}

	@PutMapping("/admin/updatepaymentmodename/paymentcode-{code}")
	@Operation(summary = "update data of paymentmodes table", description = "updates amount column in row into paymentmodes table")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetched data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed"), })
	public String updatePaymentModeName(@PathVariable("code") String code,
			@Valid @RequestBody PaymentMode newPaymentMode) {
		if (code == null || code.isBlank()) {
			return "id cannot be null or blank";
		}
		var optionalPaymentMode = paymentrepo.findById(code);
		if (optionalPaymentMode.isPresent()) {

			var paymentMode = optionalPaymentMode.get();
			paymentMode.setPaymentName(newPaymentMode.getPaymentName());
			paymentrepo.save(paymentMode);
			return "updated";
		}

		return "Code not found in the table";
	}

}
