package br.com.apiEM.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
  
}
