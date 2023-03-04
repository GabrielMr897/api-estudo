package br.com.apiEM.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  List<User> findByIsActive(Boolean isActive);

  Boolean existsByEmailIgnoreCase(String email);

  Boolean existsByCpf(String cpf);

}
