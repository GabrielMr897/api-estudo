package br.com.apiEM.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.apiEM.exception.DescriptionVehicleException;
import br.com.apiEM.model.DescriptionVehicle;
import br.com.apiEM.model.Vehicle;
import br.com.apiEM.repository.DescriptionVehicleRepository;
import br.com.apiEM.repository.VehicleRepository;
import jakarta.transaction.Transactional;

@Service
public class DescriptionVehicleService {
  
  @Autowired
  private DescriptionVehicleRepository descriptionVehicleRepository;


  @Autowired
  private VehicleRepository vehicleRepository;

  public List<DescriptionVehicle> findAll() {

    return descriptionVehicleRepository.findAll();
  }

  @Transactional
  public DescriptionVehicle register(DescriptionVehicle descriptionVehicle) {

    Long v = descriptionVehicle.getVehicle().getId();
    
    
    vehicleRepository.findById(v).orElseThrow(() -> new DescriptionVehicleException("not find vehicle id" + v));


    return descriptionVehicleRepository.save(descriptionVehicle);

  }

  @Transactional
  public DescriptionVehicle update(DescriptionVehicle descriptionVehicle, Long id) {

    DescriptionVehicle descV = descriptionVehicleRepository.findById(id).orElseThrow(() -> new DescriptionVehicleException("not find description id " + id));

    Vehicle v = vehicleRepository.findById(descriptionVehicle.getVehicle().getId()).orElseThrow(() -> new DescriptionVehicleException("not find vehicle id" + descriptionVehicle.getVehicle().getId()));

    descV.setVehicle(v);

    if(descriptionVehicle.getDescription() != null) {
      descV.setDescription(descriptionVehicle.getDescription());
    }

    return descriptionVehicleRepository.save(descV);
  }

  @Transactional
  public void delete(Long id) {

     descriptionVehicleRepository.findById(id).orElseThrow(() -> new DescriptionVehicleException("not find description id " + id));

    descriptionVehicleRepository.deleteById(id);
  }
}
