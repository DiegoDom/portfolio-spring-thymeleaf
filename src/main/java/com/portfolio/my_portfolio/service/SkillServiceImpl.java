package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.repository.SkillRepositoryImpl;

@Service
public class SkillServiceImpl implements ISkillService {
  private final SkillRepositoryImpl skillRepository;

  public SkillServiceImpl(SkillRepositoryImpl skillRepository) {
    this.skillRepository = skillRepository;
  }

  @Override
  public Skill save(Skill skill) {
    return skillRepository.save(skill);
  }

  @Override
  public Optional<Skill> findById(Long id) {
    return skillRepository.findById(id);
  }

  @Override
  public List<Skill> findAll() {
    return skillRepository.findAll();
  }

  @Override
  public void deleteById(Long id) {
    skillRepository.deleteById(id);
  }
}
