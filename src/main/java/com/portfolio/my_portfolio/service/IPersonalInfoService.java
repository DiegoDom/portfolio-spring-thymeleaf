package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.PersonalInfo;

public interface IPersonalInfoService {
  PersonalInfo save(PersonalInfo personalInfo);

  Optional<PersonalInfo> findById(Long id);

  List<PersonalInfo> findAll();

  void deleteById(Long id);

}
