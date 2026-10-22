package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.repository.PersonalInfoRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonalInfoServiceImpl implements IPersonalInfoService {

  private final PersonalInfoRepositoryImpl personalInfoRepository;
  private final Validator validator;

  @Override
  @Transactional
  public PersonalInfo save(PersonalInfo personalInfo) {
    BindingResult result = new BeanPropertyBindingResult(personalInfo, "personalInfo");
    validator.validate(personalInfo, result);
    System.out.println(result.getAllErrors());
    if (result.hasErrors()) {
      throw new ValidationException(result);
    }

    return personalInfoRepository.save(personalInfo);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PersonalInfo> findById(Long id) {
    return personalInfoRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PersonalInfo> findAll() {
    return personalInfoRepository.findAll();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    personalInfoRepository.deleteById(id);
  }

}
