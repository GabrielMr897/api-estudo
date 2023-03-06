package br.com.apiEM.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.apiEM.exception.CategoriesException;
import br.com.apiEM.model.Categories;
import br.com.apiEM.repository.CategoriesRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoriesService {
  
  @Autowired
  private CategoriesRepository categoriesRepository;



  public List<Categories> findAll() {
    
    return categoriesRepository.findAll();
  }



  public Categories findById(Long id) {
    return categoriesRepository.findById(id).orElseThrow(() -> new CategoriesException("not find id, " + id));
  }

  @Transactional
  public Categories registerCategories(Categories categories) {

    if(categoriesRepository.existsByNameIgnoreCase(categories.getName())) {
      throw new CategoriesException("name already exists, " + categories.getName());
    }

    Categories c = new Categories();
    c.setName(categories.getName());  
    c = categoriesRepository.save(c);

    return c;
  }

  @Transactional
  public Categories updCategories(Long id, Categories categories) {


    Categories c = categoriesRepository.findById(id).orElseThrow(() -> new CategoriesException("not find id, " + id));

    if(!categories.getName().equalsIgnoreCase(categories.getName()) && categoriesRepository.existsByNameIgnoreCase(categories.getName())) {
      throw new CategoriesException("name already exists" + categories.getName());
    }

    c.setName(categories.getName());
    c = categoriesRepository.save(c);

    return c;
  }


  @Transactional
  public void delete(Long id) {
    Categories c = categoriesRepository.findById(id)
        .orElseThrow(() -> new CategoriesException("not find id,  " + id));

    if (c.getVehicles().size() > 0) {
      throw new CategoriesException("Category has elements");
    }

    categoriesRepository.deleteById(id);
  }


  public List<Categories> findByName(String name) {
    List<Categories> categories = categoriesRepository.findByNameEqualsIgnoreCase(name);
    if (categories.isEmpty()) {
      throw new CategoriesException("not find name, " + name);
    }
    return categories;
  }



}
