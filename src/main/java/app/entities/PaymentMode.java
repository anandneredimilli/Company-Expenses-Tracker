package app.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "paymentmodes")
public class PaymentMode {
	@Id
	@Size(max = 7, message = "size must be less than 7")
	@Column(name = "Paymentcode")
	private String paymentCode;

	@NotBlank(message = "blank or null payment name is not allowed")
	@Size(max = 50, message = "payment name should be less than 50")
	@Column(name = "Paymentname")
	private String paymentName;

	@Column(name = "Remarks")
	private String remarks;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentmode")
	@JsonIgnore
	private List<Expenditure> expenditure = new ArrayList<>();

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Expenditure> getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(List<Expenditure> expenditure) {
		this.expenditure = expenditure;
	}

}