package br.com.apiEM.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
  
  @NotBlank
  private String refreshToken;
}
