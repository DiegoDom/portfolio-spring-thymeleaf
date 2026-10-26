package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.portfolio.my_portfolio.model.User;

public interface IUserService extends UserDetailsService {
  User save(User user);

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long id);

  List<User> findAll();

  void deleteById(Long id);
}
