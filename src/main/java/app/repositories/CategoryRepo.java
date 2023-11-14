package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, String> {

	void deleteByCategoryCode(String s);

}
