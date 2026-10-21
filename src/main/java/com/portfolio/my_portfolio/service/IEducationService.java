package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Education;

public interface IEducationService {
  Education save(Education education);

  Optional<Education> findById(Long id);

  List<Education> findAll();

  void deleteById(Long id);
}
