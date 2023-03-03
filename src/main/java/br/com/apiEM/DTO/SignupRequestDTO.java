package br.com.apiEM.DTO;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDTO {

  @NotBlank
  @Size(min = 3, max = 15)
  private String username;

  @NotBlank
  @CPF
  private String cpf;

  @NotBlank
  private String nameC;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;

  @NotBlank
  private String number;

  @NotBlank
  @Past
  private LocalDate dateOfBirth;

}
