package br.com.apiEM.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;


@Data
@Entity
@Table(name = "user_login")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  private String login;


  @Column
  @CPF
  @NotBlank
  private String cpf;
  

  @Column(name = "name_c")
  @NotBlank
  private String nameC;


  @Column
  @NotBlank
  private String number;


  @Column(name = "date_of_birth")
  @NotBlank
  @Past
  private LocalDate dateOfBirth;

  @Column
  @NotBlank
  private String password;

  @Column
  private String foto;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "role_user", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<Role> roles = new HashSet<>();



  public User(Long id, @NotBlank String login, @CPF @NotBlank String cpf, @NotBlank String nameC,
      @NotBlank String number, @NotBlank @Past LocalDate dateOfBirth, @NotBlank String password, String foto,
      Set<Role> roles) {
    this.id = id;
    this.login = login;
    this.cpf = cpf;
    this.nameC = nameC;
    this.number = number;
    this.dateOfBirth = dateOfBirth;
    this.password = password;
    this.foto = foto;
    this.roles = roles;
  }



  public User() {

  }

  
}
