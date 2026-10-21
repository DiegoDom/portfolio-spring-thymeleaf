package com.portfolio.my_portfolio.repository;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Education;

public interface IEducationRepository {
  Education save(Education Education);

  Optional<Education> findById(Long id);

  List<Education> findAll();

  void deleteById(Long id);
}
