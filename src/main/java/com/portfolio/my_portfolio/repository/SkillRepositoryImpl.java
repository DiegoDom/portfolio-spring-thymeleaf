package com.portfolio.my_portfolio.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.portfolio.my_portfolio.model.Skill;

@Repository
@RequiredArgsConstructor
public class SkillRepositoryImpl implements ISkillRepository {
  private final String TABLE_NAME = "skills";
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Skill> skillRowMapper = (rs, numRow) -> {
    Skill skill = new Skill();
    skill.setId(rs.getLong("id"));
    skill.setName(rs.getString("name"));
    skill.setLevelPercentage(rs.getInt("level_percentage"));
    skill.setIconClass(rs.getString("icon_class"));
    skill.setPersonalInfoId(rs.getLong("personal_info_id"));

    return skill;
  };

  @Override
  public Skill save(Skill skill) {
    if (skill.getId() == null) {
      String sqlString = "INSERT INTO " + TABLE_NAME
          + " (name, level_percentage, icon_class, personal_info_id) VALUES (?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sqlString, new String[] { "id" });
        ps.setString(1, skill.getName());
        ps.setInt(2, skill.getLevelPercentage());
        ps.setString(3, skill.getIconClass());
        ps.setLong(4, skill.getPersonalInfoId());
        return ps;
      }, keyHolder);

      Number generatedId = Objects.requireNonNull(
          keyHolder.getKey(),
          "The database did not return a generated ID");

      skill.setId(generatedId.longValue());
    } else {
      String sqlString = "UPDATE " + TABLE_NAME
          + " SET name = ?, level_percentage = ?, icon_class = ?, personal_info_id = ? WHERE id = ?";
      jdbcTemplate.update(sqlString, skill.getName(), skill.getLevelPercentage(), skill.getIconClass(),
          skill.getPersonalInfoId(), skill.getId());
    }
    return skill;
  }

  @Override
  public Optional<Skill> findById(Long id) {
    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, skillRowMapper, id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Skill> findAll() {
    String sqlString = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(sqlString, skillRowMapper);
  }

  @Override
  public void deleteById(Long id) {
    String sqlString = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    jdbcTemplate.update(sqlString, id);
  }

}
