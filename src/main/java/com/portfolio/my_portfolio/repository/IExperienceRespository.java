package com.portfolio.my_portfolio.repository;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Experience;

public interface IExperienceRespository {
  Experience save(Experience experience);

  Optional<Experience> findById(Long id);

  List<Experience> findAll();

  void deleteById(Long id);
}
