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

import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.service.SkillServiceImpl;

@RestController
@RequestMapping("/api/test/skills")
public class SkillTestController {
  private final SkillServiceImpl skillService;

  public SkillTestController(SkillServiceImpl skillService) {
    this.skillService = skillService;
  }

  @GetMapping("/all")
  public List<Skill> findAll() {
    return skillService.findAll();
  }

  @GetMapping("{id}")
  public Skill findById(@PathVariable Long id) {
    Optional<Skill> skill = skillService.findById(id);

    if (skill.isPresent()) {
      return skill.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<Skill> create(@RequestBody Skill skill) {
    Skill savedSkill = skillService.save(skill);
    return new ResponseEntity<>(savedSkill, HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    Optional<Skill> skill = skillService.findById(id);
    if (skill.isPresent()) {
      skillService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id);
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<Skill> update(@PathVariable Long id, @RequestBody Skill skill) {
    Optional<Skill> existingSkill = skillService.findById(id);
    if (existingSkill.isPresent()) {
      skill.setId(id);
      Skill updatedSkill = skillService.save(skill);
      return new ResponseEntity<>(updatedSkill, HttpStatus.OK);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id);
    }
  }
}
