package br.com.apiEM.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table
public class Address {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Size(max = 2, min = 2)
  private String uf;

  @Column
  private String city;
  
  @Column
  private String cep;

  @Column
  private String neighborhood;

  private String street;

  private String number;

  private String complement;

  private Boolean isActive;


  public Address(Long id, @Size(max = 2, min = 2) String uf, String city, String cep, String neighborhood,
      String street, String number, String complement) {
    this.id = id;
    this.uf = uf;
    this.city = city;
    this.cep = cep;
    this.neighborhood = neighborhood;
    this.street = street;
    this.number = number;
    this.complement = complement;
  }
  
}
