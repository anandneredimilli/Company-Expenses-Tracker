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
@Table(name = "categories")
public class Category {

	@Id
	@NotBlank(message = "categoryCode cannot be null")
	@Size(max = 10)
	@Column(name = "categorycode")
	private String categoryCode;

	@Size(max = 30)
	@Column(name = "categoryname")
	private String categoryName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	@JsonIgnore
	private List<Expenditure> expenditure = new ArrayList<>();

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Expenditure> getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(List<Expenditure> expenditure) {
		this.expenditure = expenditure;
	}

}
