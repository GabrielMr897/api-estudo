package br.com.apiEM.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
  
}
