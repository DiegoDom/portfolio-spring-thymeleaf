package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Skill;

public interface ISkillService {
  Skill save(Skill skill);

  Optional<Skill> findById(Long id);

  List<Skill> findAll();

  void deleteById(Long id);
}
