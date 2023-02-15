package br.com.apiEM.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.apiEM.exception.VehicleException;
import br.com.apiEM.model.Vehicle;
import br.com.apiEM.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicle")
@Tag(name = "Vehicles", description = "Controller Vehicles")
public class VehicleController {
  
  @Autowired
  private VehicleService vehicleService;


  @GetMapping
  @Operation(summary = "Get all Vehicles", description = "findAll vehicles", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get all!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehicle.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<List<Vehicle>> findAll() {
    return ResponseEntity.ok(vehicleService.findAll());
  }

  @GetMapping("{id}")
  @Operation(summary = "Get vehicle by id", description = "find vehicles by id", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get vehicles by id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehicle.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> findById(@PathVariable Long id) {

    try {
      return ResponseEntity.ok(vehicleService.findById(id));
    } catch (VehicleException v) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
  @PostMapping("/create")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Create Vehicle", responses = {
    @ApiResponse(responseCode = "201", description = "Successfully Register!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehicle.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> createV(@RequestParam("file") MultipartFile file, @Valid @RequestPart(value = "vehicle") Vehicle vehicle) throws IOException {
    try {
      Vehicle v = vehicleService.createV(vehicle, file);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(v.getId()).toUri();

      return ResponseEntity.created(uri).body(v);
    } catch (IOException | VehicleException | MaxUploadSizeExceededException e) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
  @PutMapping("/update/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Update Vehicle", responses = {
    @ApiResponse(responseCode = "201", description = "Successfully Updated!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehicle.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> updateV(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle) {

    try {
      Vehicle v = vehicleService.updateV(id, vehicle);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(v.getId()).toUri();

      return ResponseEntity.created(uri).body(v);
    } catch (VehicleException v) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
  @PutMapping("/update-img/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Update Vehicle image", responses = {
    @ApiResponse(responseCode = "201", description = "Successfully Updated!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vehicle.class))),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> updateVImg(@PathVariable Long id, @RequestParam(name = "file") MultipartFile file) {
    try {
      Vehicle vehicle = vehicleService.updateVImg(id, file);
      return ResponseEntity.ok(vehicle);
    } catch (VehicleException | IOException | MaxUploadSizeExceededException e) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/delete/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Delete vehicle", responses = {
    @ApiResponse(responseCode = "204", description = "Successfully Deleted!"),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> deleteV(@PathVariable Long id) {
    try {
      vehicleService.deleteVehicle(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (VehicleException e) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/reactive-v/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Reactive vehicle", responses = {
    @ApiResponse(responseCode = "204", description = "Successfully Reactivated!"),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "401", ref = "badcredentials"),
    @ApiResponse(responseCode = "403", ref = "forbidden"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> reactiveV(@PathVariable Long id) {
    try {
      vehicleService.reactiveV(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (VehicleException e) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @GetMapping("/search")
  @Operation(summary = "Search vehicle pageable", description = "Get all vehicles active", responses = {
    @ApiResponse(responseCode = "200", description = "Successfully get all!"),
    @ApiResponse(responseCode = "400", ref = "BadRequest"),
    @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
    @ApiResponse(responseCode = "500", ref = "internalServerError")
})
  public ResponseEntity<Object> searchVehicleAndVehicleBrand(@RequestParam(required = false) String brand, @RequestParam(required = false) String model, @RequestParam(required = true) Boolean isActive, @PageableDefault(page = 0, size = 15) Pageable p) {
    try {

      if(model == null && brand == null ) {
        ResponseEntity.ok(vehicleService.findAllByActive(isActive, p));
      }

      if(model == null) {
        return ResponseEntity.ok(vehicleService.findAllVehicleBrand(brand, isActive, p));
      }

      if(brand == null) {
        return ResponseEntity.ok(vehicleService.searchVehicle(model, isActive, p));
      }

      return ResponseEntity.ok(vehicleService.searchVehicleByBrand(model, brand, isActive, p));
     } catch (VehicleException v) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
     }
  }



}
