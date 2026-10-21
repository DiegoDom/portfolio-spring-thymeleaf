package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Project;

public interface IProjectService {
  Project save(Project project);

  Optional<Project> findById(Long id);

  List<Project> findAll();

  void deleteById(Long id);
}
