package br.com.apiEM.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.enums.RoleEnum;
import br.com.apiEM.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleEnum roleUser);
  
}
