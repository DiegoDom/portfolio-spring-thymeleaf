package com.portfolio.my_portfolio.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.portfolio.my_portfolio.model.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositotyImpl implements IUserRepository {

  private static final String TABLE_NAME = "users";
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<User> userRowMapper = (rs, numRow) -> {
    User user = new User();
    user.setId(rs.getLong("id"));
    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setEnabled(rs.getBoolean("enabled"));

    return user;
  };

  @Override
  public User save(User user) {
    if (user.getId() == null) {
      String sqlString = "INSERT INTO " + TABLE_NAME
          + " (username, password, enabled) VALUES (?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sqlString, new String[] { "id" });
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setBoolean(3, user.isEnabled());
        return ps;
      }, keyHolder);

      Number generatedId = Objects.requireNonNull(
          keyHolder.getKey(),
          "The database did not return a generated ID");

      user.setId(generatedId.longValue());
    } else {
      String sqlString = "UPDATE " + TABLE_NAME
          + " SET username = ?, password = ?, enabled = ? WHERE id = ?";
      jdbcTemplate.update(sqlString, user.getUsername(), user.getPassword(), user.isEnabled(), user.getId());
    }
    return user;
  }

  @Override
  public Optional<User> findByUsername(String username) {
    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, userRowMapper, username));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> findById(Long id) {
    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, userRowMapper, id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<User> findAll() {
    String sqlString = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(sqlString, userRowMapper);
  }

  @Override
  public void deleteById(Long id) {
    String sqlString = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    jdbcTemplate.update(sqlString, id);
  }

}
