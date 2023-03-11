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
import br.com.apiEM.model.Address;
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
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public SignupRegisterResponseDTO registerUser(SignupRequestDTO signUpRequest) {
        System.out.println("entrei");
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new AuthException("Error: Username já em uso!");
        }

        if (userRepository.existsByEmailIgnoreCase(signUpRequest.getEmail())) {
            throw new AuthException("Error: Email já em uso!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setCpf(signUpRequest.getCpf());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setNameC(signUpRequest.getNameC());
        user.setNumber(signUpRequest.getNumber());
        user.setFoto("https://th.bing.com/th/id/R.7042e85177b903f3ccd72d77daf9824e?rik=rKmaAIgN5Aua0g&pid=ImgRaw&r=0");
        user.setIsActive(true);

        Address address = addressService.register(signUpRequest.getAddress());

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        user.setAddress(address);
        userRepository.save(user);

        List<RoleEnum> rolesList = roles.stream().map(Role::getName).collect(Collectors.toList());

        return new SignupRegisterResponseDTO(user, rolesList);
    }

    @Transactional
    public String logoutUser() {
        UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return "Log out successful!";
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
    public SignupRegisterResponseDTO removeRoles(RoleRequestDTO rolesIn, Long idUsuario) {
        Optional<User> user = userRepository.findById(idUsuario);

        if (!user.isPresent()) {
            throw new AuthException("Error: User notFound");
        }

        Set<String> strRoles = rolesIn.getRoles();
        Set<Role> roles = new HashSet<>();

        for (String role : strRoles) {
            RoleEnum roleE;
            switch (role) {
                case "admin":
                    roleE = RoleEnum.ROLE_ADMIN;
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
                throw new AuthException("Error: ROLE_USER cannot be removed");
            }
        }

        currentRoles.removeAll(roles);
        user.get().setRoles(currentRoles);
        userRepository.save(user.get());

        List<RoleEnum> rolesList = currentRoles.stream().map(Role::getName).collect(Collectors.toList());

        return new SignupRegisterResponseDTO(user.get(), rolesList);
    }

    @Transactional
    public SignupRegisterResponseDTO newRoles(RoleRequestDTO rolesIn, Long idUsuario) {
        Optional<User> user = userRepository.findById(idUsuario);

        if (!user.isPresent()) {
            throw new AuthException("Error: User notFound");
        }

        Set<String> strRoles = rolesIn.getRoles();
        Set<Role> roles = new HashSet<>();

        for (String role : strRoles) {
            RoleEnum roleE;
            switch (role) {
                case "admin":
                    roleE = RoleEnum.ROLE_ADMIN;
                    break;
                default:
                    roleE = RoleEnum.ROLE_USER;
            }
            Role foundRole = roleRepository.findByName(roleE)
                    .orElseThrow(() -> new AuthException("Error: Role is not found."));
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
    public SignupResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

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