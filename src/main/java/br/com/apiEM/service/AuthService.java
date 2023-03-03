package br.com.apiEM.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.apiEM.DTO.LoginRequestDTO;
import br.com.apiEM.DTO.RefreshTokenRequestDTO;
import br.com.apiEM.DTO.RefreshTokenResponseDTO;
import br.com.apiEM.DTO.RoleRequestDTO;
import br.com.apiEM.DTO.SignupRegisterResponseDTO;
import br.com.apiEM.DTO.SignupRequestDTO;
import br.com.apiEM.DTO.SignupResponseDTO;
import br.com.apiEM.enums.RoleEnum;
import br.com.apiEM.exception.AuthException;
import br.com.apiEM.exception.RefreshTokenException;
import br.com.apiEM.model.RefreshToken;
import br.com.apiEM.model.Role;
import br.com.apiEM.model.User;
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


  @Transactional
  public SignupRegisterResponseDTO registerUser(SignupRequestDTO signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new AuthException("Username is already taken!");
    }

    if (userRepository.existsByEmailIgnoreCase(signUpRequest.getEmail())) {
      throw new AuthException("Email is already in use!");
    }

    

    User user = new User(signUpRequest.getUsername(), signUpRequest.getCpf(), signUpRequest.getEmail(),
    signUpRequest.getNameC(), signUpRequest.getNumber(), signUpRequest.getDateOfBirth(), encoder.encode(signUpRequest.getPassword()),
    true, "https://th.bing.com/th/id/R.7042e85177b903f3ccd72d77daf9824e?rik=rKmaAIgN5Aua0g&pid=ImgRaw&r=0");

    Set<Role> roles = new HashSet<>();

    Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);

    user.setRoles(roles);
    userRepository.save(user);

    List<RoleEnum> rolesList = roles.stream().map(Role::getName).collect(Collectors.toList());

   

    return new SignupRegisterResponseDTO(user, rolesList);
  }

  @Transactional
  public SignupRegisterResponseDTO newRoles(RoleRequestDTO rolesI, Long idUsuario) {
    Optional<User> user = userRepository.findById(idUsuario);

    if (!user.isPresent()) {
      throw new AuthException("Error: User notFound");
    }

    Set<String> strRoles = rolesI.getRoles();
    Set<Role> roles = new HashSet<>();

    for (String role : strRoles) {
      RoleEnum roleE;
      switch (role) {
        case "admin":
          roleE = RoleEnum.ROLE_ADMIN;
          break;
        case "vendor": 
          roleE = RoleEnum.ROLE_VENDOR;
          break;
        default:
          roleE = RoleEnum.ROLE_USER;
      }
      Role foundRole = roleRepository.findByName(roleE)
          .orElseThrow(() -> new AuthException("Role is not found."));
      roles.add(foundRole);
    }

    Set<Role> currentRoles = user.get().getRoles();
    currentRoles.addAll(roles);
    user.get().setRoles(currentRoles);
    userRepository.save(user.get());

    List<RoleEnum> rolesList = currentRoles.stream().map(Role::getName).collect(Collectors.toList());

    return new SignupRegisterResponseDTO(user.get(), rolesList);
  }

  @Transactional
  public SignupRegisterResponseDTO removeRoles(RoleRequestDTO rolesIn, Long idUsuario) {
    Optional<User> user = userRepository.findById(idUsuario);

    if (!user.isPresent()) {
      throw new AuthException("User not found");
    }

    Set<String> strRoles = rolesIn.getRoles();
    Set<Role> roles = new HashSet<>();

    for (String role : strRoles) {
      RoleEnum roleE;
      switch (role) {
        case "admin":
          roleE = RoleEnum.ROLE_ADMIN;
          break;
        case "vendor":
          roleE = RoleEnum.ROLE_VENDOR;
          break;
        default:
          roleE = null;
      }
      Role foundRole = roleRepository.findByName(roleE)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(foundRole);
    }

    Set<Role> currentRoles = user.get().getRoles();
    for (Role role : roles) {
      if (role.getName().equals(RoleEnum.ROLE_USER) && !currentRoles.contains(role)) {
        throw new AuthException("role user cannot be removed");
      }
    }

    currentRoles.removeAll(roles);
    user.get().setRoles(currentRoles);
    userRepository.save(user.get());

    List<RoleEnum> rolesList = currentRoles.stream().map(Role::getName).collect(Collectors.toList());

    return new SignupRegisterResponseDTO(user.get(), rolesList);
  }


  @Transactional
  public RefreshTokenResponseDTO refreshtoken(RefreshTokenRequestDTO request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          if (!user.getIsActive()) {
            refreshTokenService.deleteByUserId(user.getId());
            throw new RefreshTokenException(requestRefreshToken, "Refresh token is not in database!");
          }
          String token = jwtUtils.generateTokenFromUsername(user.getUsername(), user.getId());
          List<Role> roles = user.getRoles().stream().collect(Collectors.toList());
          List<RoleEnum> rolesList = roles.stream().map(Role::getName).collect(Collectors.toList());
          return new RefreshTokenResponseDTO(token, requestRefreshToken, user.getId(),
              user.getUsername(), user.getEmail(), rolesList);
        })
        .orElseThrow(() -> new RefreshTokenException(requestRefreshToken,
            "Refresh token is not in database!"));
  }


  @Transactional
  public String logoutUser() {
    UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return "Log out successful!";
  }

}
