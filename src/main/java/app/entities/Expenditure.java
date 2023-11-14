package app.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Expenditures")
public class Expenditure {
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Size(max = 10)
	@NotBlank(message = "category code cannot be null")
	@Column(name = "categorycode")
	private String categoryCode;

	@Size(max = 6)
	@NotBlank(message = "department code cannot be null")
	@Column(name = "departmentcode")
	private String departmentCode;

	@Column(name = " Amount")
	private Double amount;

	@Column(name = " Expendituredate")
	private LocalDate expenditureDate;

	@Size(max = 50)
	@Column(name = "Authorizedby")
	private String authorizedBy;

	@Column(name = "Description")
	private String description;

	@Size(max = 7)
	@Column(name = "Paymentmode")
	private String paymentMode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "categorycode", insertable = false, updatable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "departmentcode", insertable = false, updatable = false)
	private Department department;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "paymentmode", insertable = false, updatable = false)
	private PaymentMode paymentmode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getExpenditureDate() {
		return expenditureDate;
	}

	public void setExpenditureDate(LocalDate expenditureDate) {
		this.expenditureDate = expenditureDate;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public PaymentMode getPaymentmode() {
		return paymentmode;
	}

	public void setPaymentmode(PaymentMode paymentmode) {
		this.paymentmode = paymentmode;
	}

	@Override
	public String toString() {
		return "Expenditure [id=" + id + ", categoryCode=" + categoryCode + ", departmentCode=" + departmentCode
				+ ", amount=" + amount + ", expenditureDate=" + expenditureDate + ", authorizedBy=" + authorizedBy
				+ ", description=" + description + ", paymentMode=" + paymentMode + "]";
	}

}
