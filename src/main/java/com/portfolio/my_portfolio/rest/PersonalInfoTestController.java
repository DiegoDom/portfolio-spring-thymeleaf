package com.portfolio.my_portfolio.rest;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.service.PersonalInfoServiceImpl;

@RestController
@RequestMapping("/api/test/personal-info")
public class PersonalInfoTestController {

  private final PersonalInfoServiceImpl personalInfoService;

  public PersonalInfoTestController(PersonalInfoServiceImpl personalInfoService) {
    this.personalInfoService = personalInfoService;
  }

  @GetMapping
  public PersonalInfo find() {
    Optional<PersonalInfo> personalInfo = personalInfoService.find();

    if (personalInfo.isPresent()) {
      return personalInfo.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PersonalInfo not found");
    }
  }

  @PutMapping
  public PersonalInfo save(@Valid @RequestBody PersonalInfo personalInfo) {
    return personalInfoService.save(personalInfo);
  }

}
