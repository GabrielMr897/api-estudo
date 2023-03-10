package br.com.apiEM.model;

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
@Table(name = "description_vehicle")
public class DescriptionVehicle {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  private String description;
  
  @ManyToOne
  @JoinColumn(name = "id_vehicle", referencedColumnName = "id")
  private Vehicle vehicle;

  public DescriptionVehicle(Long id, @NotBlank String description, Vehicle vehicle) {
    this.id = id;
    this.description = description;
    this.vehicle = vehicle;
  }

  
}
