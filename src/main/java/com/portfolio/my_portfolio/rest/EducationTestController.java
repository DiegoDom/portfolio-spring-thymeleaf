package com.portfolio.my_portfolio.rest;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.service.EducationServiceImpl;

@RestController
@RequestMapping("/api/test/educations")
public class EducationTestController {
  private final EducationServiceImpl educationService;

  public EducationTestController(EducationServiceImpl educationService) {
    this.educationService = educationService;
  }

  @GetMapping("/all")
  public List<Education> findAll() {
    return educationService.findAll();
  }

  @GetMapping("{id}")
  public Education findById(@PathVariable Long id) {
    Optional<Education> education = educationService.findById(id);

    if (education.isPresent()) {
      return education.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<Education> create(@Valid @RequestBody Education education) {
    Education savedEducation = educationService.save(education);
    return new ResponseEntity<>(savedEducation, HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    Optional<Education> education = educationService.findById(id);
    if (education.isPresent()) {
      educationService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<Education> update(@PathVariable Long id, @Valid @RequestBody Education education) {
    Optional<Education> existingEducation = educationService.findById(id);
    if (existingEducation.isPresent()) {
      education.setId(id);
      Education updatedEducation = educationService.save(education);
      return new ResponseEntity<>(updatedEducation, HttpStatus.OK);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }
}
