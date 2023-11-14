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
@Table(name = "departments")
public class Department {

	@Id
	@NotBlank(message = "departmentCode cannot be null")
	@Size(max = 6)
	@Column(name = "departmentcode")
	private String departmentCode;

	@Size(max = 50)
	@NotBlank(message = "department name must not be blank or null")
	@Column(name = "departmentname")
	private String departmentName;

	@Size(max = 50)
	@Column(name = "hod")
	private String hod;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
	@JsonIgnore
	private List<Expenditure> expenditure = new ArrayList<>();

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String categoryName) {
		this.departmentName = categoryName;
	}

	public String getHod() {
		return hod;
	}

	public void setHod(String hod) {
		this.hod = hod;
	}

	public List<Expenditure> getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(List<Expenditure> expenditure) {
		this.expenditure = expenditure;
	}

}
