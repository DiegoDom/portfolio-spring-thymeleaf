package com.portfolio.my_portfolio.repository;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Skill;

public interface ISkillRepository {
  Skill save(Skill Skill);

  Optional<Skill> findById(Long id);

  List<Skill> findAll();

  void deleteById(Long id);
}
