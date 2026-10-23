package com.portfolio.my_portfolio.service;

import java.util.Optional;

import com.portfolio.my_portfolio.model.PersonalInfo;

public interface IPersonalInfoService {
  Optional<PersonalInfo> find();

  PersonalInfo save(PersonalInfo personalInfo);
}
