package br.com.apiEM.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.apiEM.exception.UserException;
import br.com.apiEM.model.User;
import br.com.apiEM.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
  
  @Autowired
  private UserRepository userRepository;
 
  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private FirebaseService firebaseFileService;



  public User findUserLogged() {
    
    UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().   getPrincipal();

    return userRepository.findById(userDetails.getId()).orElseThrow(() -> new UserException("not find user: " + userDetails.getId()));
  }
  

     public List<User> findAll() {

      return userRepository.findAll();
     }

     public List<User> findAllIsActive(Boolean isActive) {
        
        return userRepository.findByIsActive(isActive);
     }

    public User findById(Long id) {
      return userRepository.findById(id).orElseThrow(() -> new UserException("not find user, id: " + id));
    }

    public User findByUsername(String username) {

      return userRepository.findByUsername(username).orElseThrow(() -> new UserException("not find user, username: " + username));
    }


    @Transactional
    public User updateU(User user) {
      

      UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

        User userId = userRepository.findById(userDetails.getId())
        .orElseThrow(() -> new UserException( "not find user id, " + userDetails.getId()));

      if(!user.getUsername().equalsIgnoreCase(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
        throw new UserException("Username already exists, username: " + user.getUsername());
      }


      if(user.getUsername() != null) {
        userId.setUsername(user.getUsername());
      }

      if(user.getPassword() != null) {
        userId.setPassword(encoder.encode(user.getPassword()));
      }

      if(user.getNumber() != null) {
        userId.setNumber(user.getNumber());
      }

      if(user.getAddress() != null) {
        userId.setAddress(user.getAddress());
      }

      userId = userRepository.save(userId);


      return userId;



    }

    @Transactional
    public User updateUImg(MultipartFile file) throws IOException {
      
      UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
      User user = userRepository.findById(userDetails.getId())
        .orElseThrow(() -> new UserException(" not find user, id: " + userDetails.getId()));

      String urlF = firebaseFileService.saveFile(file);

      firebaseFileService.deletFile(user.getFoto());

      user.setFoto(
        "https://firebasestorage.googleapis.com/v0/b/apiestudo.appspot.com/o/" + urlF + "?alt=media");

      user = userRepository.save(user);

      return user;
    }

    @Transactional
    public void deleteAdmin(Long id) {

      User user = userRepository.findById(id).orElseThrow(() -> new UserException("not find user, " + id));
      user.setIsActive(false);

      userRepository.save(user);
    }


    @Transactional
    public void deleteUser() {

      UserDetailsImp userDetails = (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();


      User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new UserException("not find user, " + userDetails.getId()));

      user.setIsActive(false);

      userRepository.save(user);
    }


    @Transactional
    public void reactiveU(Long id) {

      User user = userRepository.findById(id).orElseThrow(() -> new UserException("user not found, id: " + id));

      user.setIsActive(true);

      userRepository.save(user);
    }
     
}
