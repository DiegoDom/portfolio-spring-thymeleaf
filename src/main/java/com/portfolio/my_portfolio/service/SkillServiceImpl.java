package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.repository.SkillRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements ISkillService {
  private final SkillRepositoryImpl skillRepository;
  private final Validator validator;

  @Override
  @Transactional
  public Skill save(Skill skill) {
    BindingResult result = new BeanPropertyBindingResult(skill, "skill");
    validator.validate(skill, result);

    if (result.hasErrors()) {
      throw new ValidationException(result);
    }
    return skillRepository.save(skill);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Skill> findById(Long id) {
    return skillRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Skill> findAll() {
    return skillRepository.findAll();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    skillRepository.deleteById(id);
  }
}
