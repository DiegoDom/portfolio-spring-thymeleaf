package com.portfolio.my_portfolio.rest;

import java.util.List;
import java.util.Optional;

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
  public List<Project> findAll() {
    return projectService.findAll();
  }

  @GetMapping("{id}")
  public Project findById(@PathVariable Long id) {
    Optional<Project> project = projectService.findById(id);

    if (project.isPresent()) {
      return project.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<Project> create(@RequestBody Project project) {
    Project savedProject = projectService.save(project);
    return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
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
  public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
    Optional<Project> existingProject = projectService.findById(id);
    if (existingProject.isPresent()) {
      project.setId(id);
      Project updatedProject = projectService.save(project);
      return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }
  }

}
