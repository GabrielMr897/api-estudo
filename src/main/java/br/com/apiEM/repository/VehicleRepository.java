package br.com.apiEM.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
  Page<Vehicle> findByIsActive(Boolean isActive, Pageable pageable);

  Page<Vehicle> findByNameIsActiveAndIgnoreCase(String name, Boolean isActive, Pageable pageable);
}
