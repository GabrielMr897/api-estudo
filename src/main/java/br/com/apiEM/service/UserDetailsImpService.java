package br.com.apiEM.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.apiEM.model.User;
import br.com.apiEM.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserDetailsImpService implements UserDetailsService {
  
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    

    return UserDetailsImp.build(user.get());
  }
}
