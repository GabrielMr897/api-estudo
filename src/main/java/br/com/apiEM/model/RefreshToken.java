package br.com.apiEM.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @OneToOne
  @JoinColumn(name = "id_user", referencedColumnName = "id")
  private User user;


  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiry;
}
