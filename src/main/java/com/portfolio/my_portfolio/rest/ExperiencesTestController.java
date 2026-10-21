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

import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.repository.ExperienceRepositoryImpl;

@RestController
@RequestMapping("/api/test/experiences")
public class ExperiencesTestController {
  private final ExperienceRepositoryImpl experienceRepository;

  public ExperiencesTestController(ExperienceRepositoryImpl experienceRepository) {
    this.experienceRepository = experienceRepository;
  }

  @GetMapping("/all")
  public List<Experience> findAll() {
    return experienceRepository.findAll();
  }

  @GetMapping("{id}")
  public Experience findById(@PathVariable Long id) {
    Optional<Experience> experience = experienceRepository.findById(id);

    if (experience.isPresent()) {
      return experience.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Experience not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<Experience> create(@RequestBody Experience experience) {
    Experience savedExperience = experienceRepository.save(experience);
    return new ResponseEntity<>(savedExperience, HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    Optional<Experience> experience = experienceRepository.findById(id);
    if (experience.isPresent()) {
      experienceRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Experience not found with id: " + id);
    }
  }

}
