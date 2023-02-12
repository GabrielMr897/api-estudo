package br.com.apiEM.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.DescriptionVehicle;

public interface DescriptionVehicleRepository extends JpaRepository<DescriptionVehicle, Long> {
  
}
