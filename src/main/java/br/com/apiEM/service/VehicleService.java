package br.com.apiEM.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.apiEM.exception.VehicleException;
import br.com.apiEM.model.Categories;
import br.com.apiEM.model.User;
import br.com.apiEM.model.Vehicle;
import br.com.apiEM.repository.CategoriesRepository;
import br.com.apiEM.repository.UserRepository;
import br.com.apiEM.repository.VehicleRepository;
import jakarta.transaction.Transactional;

@Service
public class VehicleService {

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FirebaseService firebaseService;

  @Autowired
  private CategoriesRepository categoriesRepository;
  
  public List<Vehicle> findAll() {
    return vehicleRepository.findAll();
  }

  public Vehicle findById(Long id) {
    return vehicleRepository.findById(id).filter(p -> p.getIsActive()).orElseThrow(() -> new VehicleException("not find vehicle, id: " + id));
  }


  public Page<Vehicle> findAllByActive(Boolean isActive, Pageable pageable) {
    Page<Vehicle> vehicles = vehicleRepository.findByIsActive(isActive, pageable);

    return vehicles;
  }

 public Page<Vehicle> searchVehicleByBrand(String model, String brand, Boolean isActive, Pageable pageable) {
    
    if(pageable == null || pageable.getPageNumber() < 0 || pageable.getPageSize() < 1) {
      throw new VehicleException("Invalid page");
    }

    Page<Vehicle> vehicles = vehicleRepository.findByModelIgnoreCaseAndBrandIgnoreCaseAndIsActive(model, brand, isActive, pageable);

    if(vehicles == null || vehicles.isEmpty()) {
      throw new VehicleException("No vehicles found, name: " + model + "isActive" + isActive);
    }
    
    return vehicles;
  }


  public Page<Vehicle> searchVehicle(String model, Boolean isActive, Pageable pageable) {

    if(pageable == null || pageable.getPageSize() < 1 || pageable.getPageNumber() < 0) {
        throw new VehicleException("Invalid page");
    }

    Page<Vehicle> vehicle = vehicleRepository.findByModelIgnoreCaseAndIsActive(model, isActive, pageable);

    if(vehicle == null || vehicle.isEmpty()) {
      throw new VehicleException("No vehicles found, model: " + model + "isActive" + isActive );
    }

    return vehicle;
  }
   

  @Transactional
  public Vehicle createV(Vehicle vehicle, MultipartFile file) throws IOException {

  
    UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        
  User user = userRepository.findById(userDetails.getId())
        .orElseThrow(() -> new VehicleException(" not find user, id: " + userDetails.getId()));
        

    Long c = vehicle.getCategories().getId();

    Categories categories = categoriesRepository.findById(c).orElseThrow(() -> new VehicleException("not find category, id:" + c));

    String urlFile = firebaseService.saveFile(file);


    Vehicle v = new Vehicle();

    v.setCategories(categories);
    v.setBrand(vehicle.getBrand());
    v.setCity(vehicle.getCity());
    v.setState(vehicle.getState());
    v.setPrice(vehicle.getPrice());
    v.setExchange(vehicle.getExchange());
    v.setKilometersR(vehicle.getKilometersR());
    v.setFuel(vehicle.getFuel());
    v.setColor(vehicle.getColor());
    v.setEngine(vehicle.getEngine());
    v.setDoors(vehicle.getDoors());
    v.setIsActive(true);
    v.setUser(user);
    v.setFoto1("https://firebasestorage.googleapis.com/v0/b/apiestudo.appspot.com/o/" + urlFile + "?alt=media");

    v = vehicle = vehicleRepository.save(v);

    return v;



  }

  public Page<Vehicle> findAllVehicleBrand(String brand, Boolean isActive, Pageable pageable) {

    Page<Vehicle> vehicle = vehicleRepository.findByBrandIgnoreCaseAndIsActive(brand, isActive, pageable);

    return vehicle;
  }

  @Transactional
  public Vehicle updateV(Long id, Vehicle vehicle) {
    
    Vehicle v = vehicleRepository.findById(id).orElseThrow(() -> new VehicleException("not find vehicle, id: " + id));

    Categories categories = categoriesRepository.findById(vehicle.getCategories().getId()).orElseThrow(() -> new VehicleException("not find category, id: " + vehicle.getCategories().getId()));



    v.setCategories(categories);

    if(vehicle.getModel() != null) {
      
      v.setModel(vehicle.getModel());
    }
    if(vehicle.getBrand() != null) {
      
      v.setBrand(vehicle.getBrand());
    }

    if(vehicle.getCity() != null) {
      v.setCity(vehicle.getCity());
    }

    if(vehicle.getState() != null) {
      v.setState(vehicle.getState());
    }

    if(vehicle.getPrice() != null) {
      
      v.setPrice(vehicle.getPrice());
    }

    if(vehicle.getExchange() != null) {

      v.setExchange(vehicle.getExchange());
    }

    if(vehicle.getKilometersR() != null) {

      v.setKilometersR(vehicle.getKilometersR());
    }

    if(vehicle.getFuel() != null) {
      
      v.setFuel(vehicle.getFuel());
    }
    if(vehicle.getColor() != null) {

      v.setColor(vehicle.getColor());
    }
    if(vehicle.getEngine() != null) {
      v.setEngine(vehicle.getEngine());
    } 
    if(vehicle.getDoors() != null) {

        v.setDoors(vehicle.getDoors());
    }

    v = vehicleRepository.save(v);

    return v;
  }

  @Transactional
  public Vehicle updateVImg(Long id, MultipartFile file) throws IOException {

    Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new VehicleException("not find vehicle, id: " + id));

    String urlFile = firebaseService.saveFile(file);

    firebaseService.deletFile(vehicle.getFoto1());

    vehicle.setFoto1("https://firebasestorage.googleapis.com/v0/b/apiestudo.appspot.com/o/" + urlFile + "?alt=media");

    vehicle = vehicleRepository.save(vehicle);

    return vehicle;
  }


  @Transactional
  public void deleteVehicle(Long id) {
    
    Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new VehicleException("not find vehicle, id: " + id));

    vehicle.setIsActive(false);

    vehicleRepository.save(vehicle);
  }


  @Transactional
  public void reactiveV(Long id) {
    
    Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new VehicleException("not find vehicle, id: " + id));

    vehicle.setIsActive(true);
    
    vehicleRepository.save(vehicle);
  }



}
