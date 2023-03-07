package br.com.apiEM.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.apiEM.exception.CategoriesException;
import br.com.apiEM.model.Categories;
import br.com.apiEM.repository.CategoriesRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoriesService {
  
  @Autowired
  private CategoriesRepository categoriesRepository;

  @Autowired
  private FirebaseService firebaseService;



  public List<Categories> findAll() {
    
    return categoriesRepository.findAll();
  }



  public Categories findById(Long id) {
    return categoriesRepository.findById(id).orElseThrow(() -> new CategoriesException("not find id, " + id));
  }

  @Transactional
  public Categories registerCategories(Categories categories, MultipartFile file) throws IOException {

    if(categoriesRepository.existsByNameIgnoreCase(categories.getName())) {
      throw new CategoriesException("name already exists, " + categories.getName());
    }

    String foto = firebaseService.saveFile(file);
    Categories c = new Categories();
    c.setFoto(foto);
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
  public Categories updFotoCategories(Long id, MultipartFile file) throws IOException {

    Categories categories = categoriesRepository.findById(id).orElseThrow(() -> new CategoriesException("not find id, " + id));
    
    String foto = firebaseService.saveFile(file);
    categories.setFoto(foto);

    categories = categoriesRepository.save(categories);

    return categories;
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
