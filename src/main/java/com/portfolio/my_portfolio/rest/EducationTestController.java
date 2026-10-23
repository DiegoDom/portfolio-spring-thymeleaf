package com.portfolio.my_portfolio.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.portfolio.my_portfolio.dto.EducationDto;
import com.portfolio.my_portfolio.dto.EducationMapper;
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
  public List<EducationDto> findAll() {
    return educationService.findAll().stream()
        .map(EducationMapper::toDto)
        .collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public EducationDto findById(@PathVariable Long id) {
    Optional<Education> education = educationService.findById(id);

    if (education.isPresent()) {
      return EducationMapper.toDto(education.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<EducationDto> create(@Valid @RequestBody EducationDto educationDto) {
    Education education = EducationMapper.toEducation(educationDto);
    Education savedEducation = educationService.save(education);
    return new ResponseEntity<>(EducationMapper.toDto(savedEducation), HttpStatus.CREATED);
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
  public ResponseEntity<EducationDto> update(@PathVariable Long id, @Valid @RequestBody EducationDto educationDto) {
    Optional<Education> existingEducation = educationService.findById(id);
    if (existingEducation.isPresent()) {
      educationDto.setId(id);
      Education education = EducationMapper.toEducation(educationDto);
      Education updatedEducation = educationService.save(education);
      return new ResponseEntity<>(EducationMapper.toDto(updatedEducation), HttpStatus.OK);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found with id: " + id);
    }
  }
}

