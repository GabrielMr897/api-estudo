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

  public Page<Vehicle> searchVehicle(String name, Boolean isActive, Pageable pageable) {
    
    if(pageable == null || pageable.getPageNumber() < 0 || pageable.getPageSize() < 1) {
      throw new VehicleException("Invalid page");
    }

    Page<Vehicle> vehicles = vehicleRepository.findByNameIsActiveAndIgnoreCase(name, isActive, pageable);

    if(vehicles == null || vehicles.isEmpty()) {
      throw new VehicleException("No vehicles found, name: " + name + "isActive" + isActive);
    }
    
    return vehicles;
  }

  @Transactional
  public Vehicle createV(Vehicle vehicle, MultipartFile file) throws IOException {


    UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    User user = userRepository.findById(userDetails.getId())
        .orElseThrow(() -> new VehicleException(" not find user, id: " + userDetails.getId()));

    Categories categories = categoriesRepository.findById(vehicle.getCategories().getId()).orElseThrow(() -> new VehicleException("not find category, id:" + vehicle.getCategories().getId()));

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
    v.setFoto1("https://firebasestorage.googleapis.com/v0/b/apiestudo.appspot.com/o/" + urlFile + "?alt=media");

    v = vehicle = vehicleRepository.save(v);

    return v;



  }

  @Transactional
  public Vehicle updateV(Long id, Vehicle vehicle) {
    
    Vehicle v = vehicleRepository.findById(id).orElseThrow(() -> new VehicleException("not find vehicle, id: " + id));


    if(vehicleRepository.existByNameIgnoreCase(vehicle.getName()) && !v.getName().equalsIgnoreCase(vehicle.getName())) {
      throw new VehicleException("name already exists in category, name: " + vehicle.getName());
    }

    Categories categories = categoriesRepository.findById(vehicle.getCategories().getId()).orElseThrow(() -> new VehicleException("not find category, id: " + vehicle.getCategories().getId()));


    v.setCategories(categories);
    v.setModel(v.getModel());
    v.setBrand(v.getBrand());
    v.setCity(v.getCity());
    v.setState(v.getState());
    v.setPrice(v.getPrice());
    v.setExchange(v.getExchange());
    v.setKilometersR(v.getKilometersR());
    v.setFuel(v.getFuel());
    v.setColor(v.getColor());
    v.setEngine(v.getEngine());
    v.setDoors(v.getDoors());
    v.isActive(v.getIsActive());

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
