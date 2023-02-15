package br.com.apiEM.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table
public class Vehicle {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  private String model;

  @Column
  @NotBlank
  private String brand;

  @Column
  @NotBlank
  private String city;

  @Column
  @NotBlank
  private String state;

  @Column
  @NotBlank
  private BigDecimal price;

  @Column
  @NotBlank
  private String exchange;

  @Column(name = "kilometers_r")
  @NotBlank
  private BigInteger kilometersR;

  @Column
  @NotBlank
  private String fuel;

  @Column
  @NotBlank
  private String color;

  @ManyToOne
  @JoinColumn(name = "id_categories", referencedColumnName = "id")
  private Categories categories;

  @Column
  @NotBlank
  private String engine;

  @Column
  private String foto1;

  @Column
  private String foto2;

  @Column
  private String foto3;

  @Column
  @NotBlank
  private Boolean isActive;

  @Column
  @NotBlank
  private String doors;


  @ManyToOne
  @NotBlank
  @JoinColumn(name = "created_user", referencedColumnName = "id")
  private User user;
  
}
