package com.portfolio.my_portfolio.repository;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.Project;

public interface IProjectRepository {
  Project save(Project project);

  Optional<Project> findById(Long id);

  List<Project> findAll();

  void deleteById(Long id);
}
