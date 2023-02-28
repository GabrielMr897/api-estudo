package br.com.apiEM.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.apiEM.DTO.LoginRequestDTO;
import br.com.apiEM.DTO.SignupResponseDTO;
import br.com.apiEM.model.RefreshToken;
import br.com.apiEM.repository.RoleRepository;
import br.com.apiEM.repository.UserRepository;
import br.com.apiEM.utils.JwtUtils;
import jakarta.transaction.Transactional;

@Service
public class AuthService {
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private RefreshTokenService refreshTokenService;


  @Transactional
  public SignupResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

   

    return new SignupResponseDTO(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
        userDetails.getEmail(), roles);
  }

  

}
