package br.com.apiEM.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.apiEM.exception.UserException;
import br.com.apiEM.model.User;
import br.com.apiEM.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Controller user")
public class UserController {
  

  @Autowired
  private UserService userService;



  @PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
  @GetMapping("/loggedU")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Get User logged", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get User!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> findLogged() {

    try {
      return ResponseEntity.ok(userService.findUserLogged());
    } catch(UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }


  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  @SecurityRequirement(name = "token")
  @Operation(summary = "Get all", description = "Admin", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get all!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<List<User>> findAll() {
    
    return ResponseEntity.ok(userService.findAll());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/isActive/{isActive}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Get all isActive", description = "Admin", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get all!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<List<User>> findAllByIsActive(@PathVariable Boolean isActive) {

    return ResponseEntity.ok(userService.findAllIsActive(isActive));
  }


  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Get by Id", description = "Admin", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get by id!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> findById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(userService.findById(id));
    } catch(UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }


  @PreAuthorize("hasRole('USER')")
  @PutMapping("/update")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Update user",responses = {
    @ApiResponse(responseCode = "200", description = "Successfully update user!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> updateU(@Valid @RequestBody User user) {

    try {
      return ResponseEntity.ok(userService.updateU(user));
    } catch (UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('USER')")
  @PutMapping("/updateImg")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Update Image user",responses = {
    @ApiResponse(responseCode = "200", description = "Successfully update image user!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> updateUImg(@PathVariable Long id, @RequestParam(name = "file") MultipartFile file) throws IOException {
    try {
      return ResponseEntity.ok(userService.updateUImg(file));
    } catch(UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/deleteAdmin")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Delete user",responses = {
    @ApiResponse(responseCode = "200", description = "Admin", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> deleteAdmin(@PathVariable Long id) {

    try {
      userService.deleteAdmin(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/deleteUser")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Delete user",responses = {
    @ApiResponse(responseCode = "200", description = "Successfully delete user!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> deleteU() {

    try {
      userService.deleteUser();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.NO_CONTENT);
    }
  }


  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/reactive-u/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Reactive vehicle", responses = {
    @ApiResponse(responseCode = "204", description = "Successfully Reactivated!"),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> reactiveU(@PathVariable Long id) {

    try {
      userService.reactiveU(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (UserException u) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
  
}
