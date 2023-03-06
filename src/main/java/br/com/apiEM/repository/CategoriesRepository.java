package br.com.apiEM.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

  boolean existsByNameIgnoreCase(String name);

  List<Categories> findByNameEqualsIgnoreCase(String name);
  
}
