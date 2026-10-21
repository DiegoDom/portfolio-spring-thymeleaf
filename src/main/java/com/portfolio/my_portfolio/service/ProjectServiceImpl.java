package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.repository.ProjectRepositoryImpl;

@Service
public class ProjectServiceImpl implements IProjectService {

  private final ProjectRepositoryImpl projectRepository;

  public ProjectServiceImpl(ProjectRepositoryImpl projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Project save(Project project) {
    return projectRepository.save(project);
  }

  @Override
  public Optional<Project> findById(Long id) {
    return projectRepository.findById(id);
  }

  @Override
  public List<Project> findAll() {
    return projectRepository.findAll();
  }

  @Override
  public void deleteById(Long id) {
    projectRepository.deleteById(id);
  }

}
