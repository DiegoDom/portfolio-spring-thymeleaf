package com.portfolio.my_portfolio.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.repository.EducationRepositoryImpl;

@RestController
@RequestMapping("/api/test/educations")
public class EducationTestController {
  private final EducationRepositoryImpl educationRepository;

  public EducationTestController(EducationRepositoryImpl educationRepository) {
    this.educationRepository = educationRepository;
  }

  @GetMapping("/all")
  public List<Education> findAll() {
    return educationRepository.findAll();
  }

  @GetMapping("{id}")
  public Education findById(@PathVariable Long id) {
    Optional<Education> education = educationRepository.findById(id);

    if (education.isPresent()) {
      return education.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<Education> create(@RequestBody Education education) {
    Education savedEducation = educationRepository.save(education);
    return new ResponseEntity<>(savedEducation, HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    Optional<Education> education = educationRepository.findById(id);
    if (education.isPresent()) {
      educationRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }
}
