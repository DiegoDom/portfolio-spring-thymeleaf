package com.portfolio.my_portfolio.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.User;
import com.portfolio.my_portfolio.repository.UserRepositotyImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

  /** Contraseñas ya codificadas (BCrypt) no se vuelven a codificar al actualizar. */
  private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]?\\$\\d{2}\\$.{53}$");

  private final UserRepositotyImpl userRepositotyImpl;
  private final Validator validator;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepositotyImpl.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
  }

  @Override
  @Transactional
  public User save(User user) {
    BindingResult result = new BeanPropertyBindingResult(user, "user");
    validator.validate(user, result);

    if (user.getUsername() != null && !user.getUsername().isBlank()) {
      userRepositotyImpl.findByUsername(user.getUsername())
          .filter(existing -> !existing.getId().equals(user.getId()))
          .ifPresent(existing -> result.rejectValue(
              "username",
              "duplicate",
              "El nombre de usuario ya está en uso"));
    }

    if (result.hasErrors()) {
      throw new ValidationException(result);
    }

    String password = user.getPassword();
    if (password != null && !BCRYPT_PATTERN.matcher(password).matches()) {
      user.setPassword(passwordEncoder.encode(password));
    }

    return userRepositotyImpl.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    return userRepositotyImpl.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findById(Long id) {
    return userRepositotyImpl.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepositotyImpl.findAll();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    userRepositotyImpl.deleteById(id);
  }
}
