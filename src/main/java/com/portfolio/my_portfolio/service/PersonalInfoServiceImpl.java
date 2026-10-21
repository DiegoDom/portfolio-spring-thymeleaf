package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.repository.PersonalInfoRepositoryImpl;

@Service
public class PersonalInfoServiceImpl implements IPersonalInfoService {

  private final PersonalInfoRepositoryImpl personalInfoRepository;

  public PersonalInfoServiceImpl(PersonalInfoRepositoryImpl personalInfoRepository) {
    this.personalInfoRepository = personalInfoRepository;
  }

  @Override
  public PersonalInfo save(PersonalInfo personalInfo) {
    return personalInfoRepository.save(personalInfo);
  }

  @Override
  public Optional<PersonalInfo> findById(Long id) {
    return personalInfoRepository.findById(id);
  }

  @Override
  public List<PersonalInfo> findAll() {
    return personalInfoRepository.findAll();
  }

  @Override
  public void deleteById(Long id) {
    personalInfoRepository.deleteById(id);
  }

}
