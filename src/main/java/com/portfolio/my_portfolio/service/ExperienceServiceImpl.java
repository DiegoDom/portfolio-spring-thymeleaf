package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.repository.ExperienceRepositoryImpl;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements IExperiencesService {
  private final ExperienceRepositoryImpl experienceRepository;
  private final Validator validator;

  @Override
  @Transactional
  public Experience save(Experience experience) {
    var violations = validator.validate(experience);
    if (!violations.isEmpty()) {
      BeanPropertyBindingResult result = new BeanPropertyBindingResult(experience, "experience");
      violations.forEach(violation -> result.rejectValue(violation.getPropertyPath().toString(),
          "", violation.getMessage()));
      throw new ValidationException(result);
    }
    return experienceRepository.save(experience);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Experience> findById(Long id) {
    return experienceRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Experience> findAll() {
    return experienceRepository.findAll();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    experienceRepository.deleteById(id);
  }

}
