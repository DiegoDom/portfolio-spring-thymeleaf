package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.repository.IProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

  private final IProjectRepository projectRepository;

  @Override
  @Transactional
  public Project save(Project project) {
    return projectRepository.save(project);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Project> findById(Long id) {
    return projectRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Project> findAll() {
    return projectRepository.findAll();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    projectRepository.deleteById(id);
  }

}
