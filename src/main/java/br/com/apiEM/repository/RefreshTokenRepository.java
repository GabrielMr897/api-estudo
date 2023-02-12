package br.com.apiEM.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.apiEM.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  
}
