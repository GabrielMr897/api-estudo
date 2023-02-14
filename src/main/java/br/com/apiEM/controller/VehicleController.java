package br.com.apiEM.controller;

import java.io.IOException;

import br.com.apiEM.service.VehicleService;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
  
  @Autowired
  private VehicleService vehicleService;


  @GetMapping
  public ResponseEntity<List<Vehicle>> findAll() {
    return ResponseEntity.ok(vehicleService.findAll());
  }

  @GetMapping("{id}")
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
  public ResponseEntity<Object> createV(@RequestParam("file") MultpartFile file, @Valid @RequestPart(value = "vehicle") Vehicle vehicle) throws IOException {
    try {
      Vehicle v = vehicleService.createProduct(vehicle, file);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(v.getId()).toUri();

      return ResponseEntity.created(uri).body(v);
    } catch (IOException | VehicleException | MaxUploadSizeExceededException e) {
      return ReponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
  @PutMapping("/update/{id}")
  @SecurityRequirement(name = "token")
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
  public ResponseEntity<Object> updateVImg(@PathVariable Long id, @RequestParam(name = "file") MultiPartFile file) {
    try {
      Vehicle vehicle = vehicleService.updateVImg(id, file);
      return ResponseEntity.ok(vehicle);
    } catch (VehicleException | IOException | MaxUploadSizeExceededException e) {
      return ReponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/delete/{id}")
  @SecurityRequirement(name = "token")
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
  public ResponseEntity<Object> reactiveV(@PathVariable Long id) {
    try {
      vehicleService.reactiveV(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (VehicleException e) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @GetMapping("/isActive")
  public ResponseEntity<Object> findAllIsActive(@RequestParam(required = true) Boolean isActive, @PageableDefault(page = 0, size = 15) Pageable p) {

    try {
      return ResponseEntity.ok(vehicleService.findAllByActive(isActive, p));
    } catch (VehicleException v) {
      return ResponseEntity.unprocessableEntity().body(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }



}
