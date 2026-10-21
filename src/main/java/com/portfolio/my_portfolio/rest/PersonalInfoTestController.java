package com.portfolio.my_portfolio.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.my_portfolio.service.PersonalInfoServiceImpl;
import com.portfolio.my_portfolio.model.PersonalInfo;

@RestController
@RequestMapping("/api/test/personal-info")
public class PersonalInfoTestController {

  private final PersonalInfoServiceImpl personalInfoService;

  public PersonalInfoTestController(PersonalInfoServiceImpl personalInfoService) {
    this.personalInfoService = personalInfoService;
  }

  @GetMapping("/all")
  public List<PersonalInfo> findAll() {
    return personalInfoService.findAll();
  }

  @GetMapping("{id}")
  public PersonalInfo findById(@PathVariable Long id) {
    Optional<PersonalInfo> personalInfo = personalInfoService.findById(id);

    if (personalInfo.isPresent()) {
      return personalInfo.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PersonalInfo not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<PersonalInfo> create(@RequestBody PersonalInfo personalInfo) {
    PersonalInfo savedPersonalInfo = personalInfoService.save(personalInfo);
    return new ResponseEntity<>(savedPersonalInfo, HttpStatus.CREATED);
  }

}
