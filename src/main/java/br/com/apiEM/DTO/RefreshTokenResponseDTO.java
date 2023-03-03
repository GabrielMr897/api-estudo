package br.com.apiEM.DTO;

import java.util.List;

import br.com.apiEM.enums.RoleEnum;
import lombok.Data;

@Data
public class RefreshTokenResponseDTO {
  private Long id;
  private String username;
  private String email;
  private List<RoleEnum> roles;
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";

  public RefreshTokenResponseDTO(String accessToken, String refreshToken, Long id, String username, String email,
      List<RoleEnum> roles) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
}
