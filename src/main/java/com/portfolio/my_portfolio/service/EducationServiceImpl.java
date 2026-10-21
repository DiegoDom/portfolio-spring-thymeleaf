package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.repository.EducationRepositoryImpl;

@Service
public class EducationServiceImpl implements IEducationService {
  private final EducationRepositoryImpl educationRepository;

  public EducationServiceImpl(EducationRepositoryImpl educationRepository) {
    this.educationRepository = educationRepository;
  }

  @Override
  public Education save(Education education) {
    return educationRepository.save(education);
  }

  @Override
  public Optional<Education> findById(Long id) {
    return educationRepository.findById(id);
  }

  @Override
  public List<Education> findAll() {
    return educationRepository.findAll();
  }

  @Override
  public void deleteById(Long id) {
    educationRepository.deleteById(id);
  }

}
