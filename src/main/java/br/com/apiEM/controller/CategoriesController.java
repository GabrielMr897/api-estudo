package br.com.apiEM.controller;

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

import br.com.apiEM.exception.CategoriesException;
import br.com.apiEM.model.Categories;
import br.com.apiEM.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "categories")
public class CategoriesController {
  
  @Autowired
  private CategoriesService categoriesService;



    @GetMapping
    @Operation(summary = "Get all Categories", responses = {
      @ApiResponse(responseCode = "200", description = "Successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categories.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<List<Categories>> findAll() {
    return ResponseEntity.ok(categoriesService.findAll());
  }




  @GetMapping("{id}")
  @Operation(summary = "Get by Id", responses = {
     @ApiResponse(responseCode = "200", description = "Successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categories.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> findById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(categoriesService.findById(id));
    } catch (CategoriesException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }


  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/register")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Create Category", responses = {
      @ApiResponse(responseCode = "201", description = "Successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categories.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "401", ref = "badcredentials"),
      @ApiResponse(responseCode = "403", ref = "forbidden"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> insert(@Valid @RequestBody Categories categories) {
    try {
      Categories response = categoriesService.registerCategories(categories);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(response.getId())
          .toUri();
      return ResponseEntity.created(uri).body(response);
    } catch (CategoriesException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }


  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/update/{id}")
  @SecurityRequirement(name = "token")
  @Operation(summary = "Update Category", description = "Update Category", responses = {
      @ApiResponse(responseCode = "201", description = "Successfully Updated!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categories.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "401", ref = "badcredentials"),
      @ApiResponse(responseCode = "403", ref = "forbidden"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> update(@PathVariable Long id,
      @Valid @RequestBody Categories categories) {
    try {
      Categories response = categoriesService.updCategories(id, categories);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(response.getId())
          .toUri();
      return ResponseEntity.created(uri).body(response);
    } catch (CategoriesException e) {
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
      categoriesService.delete(id);
      return ResponseEntity.status(HttpStatus.SC_NO_CONTENT).build();
    } catch (CategoriesException | DataIntegrityViolationException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }

  @GetMapping("/findName/{name}")
  @Operation(summary = "Get Categories by Name", responses = {
      @ApiResponse(responseCode = "200", description = "Successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categories.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequest"),
      @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
      @ApiResponse(responseCode = "500", ref = "internalServerError")
  })
  public ResponseEntity<Object> findByName(@PathVariable String name) {
    try {
      return ResponseEntity.ok(categoriesService.findByName(name));
    } catch (CategoriesException e) {
      return ResponseEntity.unprocessableEntity()
          .body(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }
  }

}

