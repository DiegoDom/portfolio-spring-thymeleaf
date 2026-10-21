package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Experience;

public interface IExperiencesService {
  Experience save(Experience experience);

  Optional<Experience> findById(Long id);

  List<Experience> findAll();

  void deleteById(Long id);
}
