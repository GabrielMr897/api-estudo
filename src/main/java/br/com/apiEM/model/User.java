package br.com.apiEM.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.br.CPF;

import com.google.firebase.database.annotations.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;


@Data
@Entity
@Table(name = "user_login", uniqueConstraints = {
  @UniqueConstraint(columnNames = "username"),
  @UniqueConstraint(columnNames = "email")
})
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  private String username;


  @Column
  @CPF
  @NotBlank
  private String cpf;

  @Column
  @Email
  @NotBlank
  private String email;
  

  @Column(name = "name_c")
  @NotBlank
  private String nameC;


  @Column
  @NotBlank
  private String number;


  @Column(name = "date_of_birth")
  @NotNull
  @Past
  private LocalDate dateOfBirth;

  @Column
  @NotBlank
  private String password;

  @Column(name = "is_active")
  @NotNull
  private Boolean isActive;

  @Column
  @NotBlank
  private String foto;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "role_user", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<Role> roles = new HashSet<>();


  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "address_id")
  private Address address;











  public User(Long id, @NotBlank String username, @CPF @NotBlank String cpf, @Email @NotBlank String email,
      @NotBlank String nameC, @NotBlank String number, @Past LocalDate dateOfBirth, @NotBlank String password,
      Boolean isActive, @NotBlank String foto, Set<Role> roles, Address address) {
    this.id = id;
    this.username = username;
    this.cpf = cpf;
    this.email = email;
    this.nameC = nameC;
    this.number = number;
    this.dateOfBirth = dateOfBirth;
    this.password = password;
    this.isActive = isActive;
    this.foto = foto;
    this.roles = roles;
    this.address = address;
  }











  public User() {

  }






 
  
}
