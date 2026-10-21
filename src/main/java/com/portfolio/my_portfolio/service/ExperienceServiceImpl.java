package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.repository.ExperienceRepositoryImpl;

public class ExperienceServiceImpl implements IExperiencesService {
  private final ExperienceRepositoryImpl experienceRepository;

  public ExperienceServiceImpl(ExperienceRepositoryImpl experienceRepository) {
    this.experienceRepository = experienceRepository;
  }

  @Override
  public Experience save(Experience experience) {
    return experienceRepository.save(experience);
  }

  @Override
  public Optional<Experience> findById(Long id) {
    return experienceRepository.findById(id);
  }

  @Override
  public List<Experience> findAll() {
    return experienceRepository.findAll();
  }

  @Override
  public void deleteById(Long id) {
    experienceRepository.deleteById(id);
  }

}
