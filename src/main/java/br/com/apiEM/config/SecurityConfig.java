package br.com.apiEM.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.apiEM.service.UserDetailsImpService;
import br.com.apiEM.utils.AuthenticationEntryPointJwt;
import br.com.apiEM.utils.AuthenticationTokenFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Autowired
  UserDetailsImpService userDetailsImpService;

  @Autowired
  private AuthenticationEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthenticationTokenFilter authenticationJwtTokenFilter() {
    return new AuthenticationTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsImpService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
        throws IOException, ServletException {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);

      Map<String, Object> body = new HashMap<>();
      body.put("status", HttpServletResponse.SC_FORBIDDEN);
      body.put("error", "Forbidden");
      body.put("message", "Access Denied");
      body.put("path", request.getServletPath());

      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);
    }
  }

  @Bean
  protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable().authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/api/vehicle/{id}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/vehicle").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/user/loggedU").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/categories/{id}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/authentication/sign-in").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/authentication/sign-up").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/authentication/refreshtoken").permitAll()
        .requestMatchers("/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/api-estudo-production.up.railway.app/**").permitAll()
        .anyRequest()
        .authenticated().and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl()).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler);

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

}
