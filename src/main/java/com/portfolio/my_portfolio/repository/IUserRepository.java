package com.portfolio.my_portfolio.repository;

import java.util.List;
import java.util.Optional;

import com.portfolio.my_portfolio.model.User;

public interface IUserRepository {
  User save(User user);

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long id);

  List<User> findAll();

  void deleteById(Long id);
}
