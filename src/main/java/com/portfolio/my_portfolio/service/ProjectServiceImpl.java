package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.repository.ProjectRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements IProjectService {

  private final ProjectRepositoryImpl projectRepository;
  private final Validator validator;

  @Override
  @Transactional
  public Project save(Project project) {
    BindingResult result = new BeanPropertyBindingResult(project, "project");
    validator.validate(project, result);

    if (result.hasErrors()) {
      throw new ValidationException(result);
    }
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
