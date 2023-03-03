package br.com.apiEM.DTO;

import java.util.List;

import br.com.apiEM.enums.RoleEnum;
import br.com.apiEM.model.User;
import lombok.Data;

@Data
public class SignupRegisterResponseDTO {
  
  private Long id;

  private String username;

  private String name;

  private String email;

  private String imageUrl;

  private List<RoleEnum> roles;

  public SignupRegisterResponseDTO(User u, List<RoleEnum> role) {
    this.id = u.getId();
    this.username = u.getUsername();
    this.name = u.getNameC();
    this.email = u.getEmail();
    this.imageUrl = u.getFoto();
    this.roles = role;
  }

}
