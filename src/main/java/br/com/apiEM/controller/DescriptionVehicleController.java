package br.com.apiEM.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.apiEM.exception.DescriptionVehicleException;
import br.com.apiEM.model.DescriptionVehicle;
import br.com.apiEM.service.DescriptionVehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/descV")
@Tag(name = "descVehicle", description = "descrição veículo")
public class DescriptionVehicleController {
  

  @Autowired
  private DescriptionVehicleService descriptionVehicleService;

  

  @GetMapping
  @Operation(summary = "Get all Description Vehicle", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DescriptionVehicle.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<List<DescriptionVehicle>> findAll() {
    return ResponseEntity.ok(descriptionVehicleService.findAll());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/register")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Create Description Vehicle", responses = {
      @ApiResponse(responseCode = "201", description = "Successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DescriptionVehicle.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "401", ref = "badcredentials"),
      @ApiResponse(responseCode = "403", ref = "forbidden"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> insert(@Valid @RequestBody DescriptionVehicle descriptionVehicle) throws IOException {
    try {
      DescriptionVehicle response = descriptionVehicleService.register(descriptionVehicle);
      
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(response.getId())
          .toUri();
      return ResponseEntity.created(uri).body(response);
    } catch (DescriptionVehicleException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/update/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Update Description Vehicle", responses = {
      @ApiResponse(responseCode = "201", description = "Successfully Updated!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DescriptionVehicle.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "401", ref = "badcredentials"),
      @ApiResponse(responseCode = "403", ref = "forbidden"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> update(@PathVariable Long id,
      @Valid @RequestBody DescriptionVehicle descriptionVehicle) {
    try {
      DescriptionVehicle response = descriptionVehicleService.update(descriptionVehicle, id);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(response.getId())
          .toUri();
      return ResponseEntity.created(uri).body(response);
    } catch (DescriptionVehicleException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }



  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/delete/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Delete", responses = {
      @ApiResponse(responseCode = "204", description = "Successfully!"),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "401", ref = "badcredentials"),
      @ApiResponse(responseCode = "403", ref = "forbidden"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    try {
      descriptionVehicleService.delete(id);
      return ResponseEntity.status(HttpStatus.SC_NO_CONTENT).build();
    } catch (DescriptionVehicleException | DataIntegrityViolationException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }


}
