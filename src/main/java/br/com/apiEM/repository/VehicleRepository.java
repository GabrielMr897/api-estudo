package br.com.apiEM.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
  
}
