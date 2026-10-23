package com.portfolio.my_portfolio.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.my_portfolio.dto.ProjectDto;
import com.portfolio.my_portfolio.dto.ProjectMapper;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.service.ProjectServiceImpl;

@RestController
@RequestMapping("/api/test/projects")
public class ProjectTestController {
  private final ProjectServiceImpl projectService;

  public ProjectTestController(ProjectServiceImpl projectService) {
    this.projectService = projectService;
  }

  @GetMapping("/all")
  public List<ProjectDto> findAll() {
    return projectService.findAll().stream()
        .map(ProjectMapper::tDto)
        .collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public ProjectDto findById(@PathVariable Long id) {
    Optional<Project> project = projectService.findById(id);

    if (project.isPresent()) {
      return ProjectMapper.tDto(project.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<ProjectDto> create(@Valid @RequestBody ProjectDto projectDto) {
    Project project = ProjectMapper.toProject(projectDto);
    Project savedProject = projectService.save(project);
    return new ResponseEntity<>(ProjectMapper.tDto(savedProject), HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    Optional<Project> project = projectService.findById(id);
    if (project.isPresent()) {
      projectService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<ProjectDto> update(@PathVariable Long id, @Valid @RequestBody ProjectDto projectDto) {
    Optional<Project> existingProject = projectService.findById(id);
    if (existingProject.isPresent()) {
      projectDto.setId(id);
      Project project = ProjectMapper.toProject(projectDto);
      Project updatedProject = projectService.save(project);
      return new ResponseEntity<>(ProjectMapper.tDto(updatedProject), HttpStatus.OK);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }
  }

}

