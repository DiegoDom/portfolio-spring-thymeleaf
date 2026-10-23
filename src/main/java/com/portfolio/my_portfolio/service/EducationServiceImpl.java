package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.repository.EducationRepositoryImpl;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements IEducationService {
  private final EducationRepositoryImpl educationRepository;
  private final Validator validator;

  @Override
  @Transactional
  public Education save(Education education) {
    var violations = validator.validate(education);
    if (!violations.isEmpty()) {
      BeanPropertyBindingResult result = new BeanPropertyBindingResult(education, "education");
      violations.forEach(violation -> result.rejectValue(violation.getPropertyPath().toString(),
          "", violation.getMessage()));
      throw new ValidationException(result);
    }
    return educationRepository.save(education);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Education> findById(Long id) {
    return educationRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Education> findAll() {
    return educationRepository.findAll();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    educationRepository.deleteById(id);
  }

}
