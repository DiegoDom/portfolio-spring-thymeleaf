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

import com.portfolio.my_portfolio.dto.SkillDto;
import com.portfolio.my_portfolio.dto.SkillMapper;
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
  public List<SkillDto> findAll() {
    return skillService.findAll().stream()
        .map(SkillMapper::toDto)
        .collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public SkillDto findById(@PathVariable Long id) {
    Optional<Skill> skill = skillService.findById(id);

    if (skill.isPresent()) {
      return SkillMapper.toDto(skill.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id);
    }
  }

  @PostMapping()
  public ResponseEntity<SkillDto> create(@Valid @RequestBody SkillDto skillDto) {
    Skill skill = SkillMapper.toSkill(skillDto);
    Skill savedSkill = skillService.save(skill);
    return new ResponseEntity<>(SkillMapper.toDto(savedSkill), HttpStatus.CREATED);
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
  public ResponseEntity<SkillDto> update(@PathVariable Long id, @Valid @RequestBody SkillDto skillDto) {
    Optional<Skill> existingSkill = skillService.findById(id);
    if (existingSkill.isPresent()) {
      skillDto.setId(id);
      Skill skill = SkillMapper.toSkill(skillDto);
      Skill updatedSkill = skillService.save(skill);
      return new ResponseEntity<>(SkillMapper.toDto(updatedSkill), HttpStatus.OK);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found with id: " + id);
    }
  }
}

