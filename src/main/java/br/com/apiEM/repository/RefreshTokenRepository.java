package br.com.apiEM.repository;


import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import br.com.apiEM.model.RefreshToken;
import br.com.apiEM.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  
  Optional<RefreshToken> findByToken(String token);

  RefreshToken findByUserAndExpiry(User user, Instant instant);

  @Modifying
  int deleteByUser(User user);
}
