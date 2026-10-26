package com.portfolio.my_portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.User;
import com.portfolio.my_portfolio.repository.IUserRepository;

class UserServiceTest extends ServiceIntegrationTestSupport {

  private static final int SEEDED_USERS = 1;

  @Autowired
  private IUserService userService;

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void save_insertsUserAndEncodesPassword() {
    User saved = userService.save(user("editor", "secret123", true));

    assertThat(saved.getId()).isNotNull();

    User reloaded = userRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getUsername()).isEqualTo("editor");
    assertThat(reloaded.isEnabled()).isTrue();
    assertThat(reloaded.getPassword()).isNotEqualTo("secret123");
    assertThat(passwordEncoder.matches("secret123", reloaded.getPassword())).isTrue();
  }

  @Test
  void save_persistsDisabledState() {
    User saved = userService.save(user("inactivo", "secret123", false));

    assertThat(userRepository.findById(saved.getId()).orElseThrow().isEnabled()).isFalse();
  }

  @Test
  void findByUsername_returnsPasswordForAuthentication() {
    userService.save(user("editor", "secret123", true));

    User found = userService.findByUsername("editor").orElseThrow();
    assertThat(passwordEncoder.matches("secret123", found.getPassword())).isTrue();
  }

  @Test
  void loadUserByUsername_returnsUserWithAdminAuthority() {
    UserDetails details = userService.loadUserByUsername("admin");

    assertThat(details.getUsername()).isEqualTo("admin");
    assertThat(details.getAuthorities())
        .extracting("authority")
        .containsExactly("ROLE_ADMIN");
    assertThat(details.isEnabled()).isTrue();
  }

  @Test
  void loadUserByUsername_missingUserThrows() {
    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nadie"));
  }

  @Test
  void save_withExistingId_updatesWithoutReencodingPassword() {
    User created = userService.save(user("editor", "secret123", true));
    String encodedPassword = userRepository.findById(created.getId()).orElseThrow().getPassword();

    created.setUsername("editor2");
    created.setEnabled(false);
    userService.save(created);

    User reloaded = userRepository.findById(created.getId()).orElseThrow();
    assertThat(reloaded.getUsername()).isEqualTo("editor2");
    assertThat(reloaded.isEnabled()).isFalse();
    assertThat(reloaded.getPassword()).isEqualTo(encodedPassword);
    assertThat(passwordEncoder.matches("secret123", reloaded.getPassword())).isTrue();
    assertThat(userService.findAll()).hasSize(SEEDED_USERS + 1);
  }

  @Test
  void save_duplicateUsernameThrowsValidationException() {
    ValidationException ex = assertThrows(ValidationException.class,
        () -> userService.save(user("admin", "secret123", true)));

    assertThat(ex.getBindingResult().getFieldError("username")).isNotNull();
    assertThat(userService.findAll()).hasSize(SEEDED_USERS);
  }

  @Test
  void save_blankUsernameThrowsAndPersistsNothing() {
    ValidationException ex = assertThrows(ValidationException.class,
        () -> userService.save(user(" ", "secret123", true)));

    assertThat(ex.getBindingResult().getFieldError("username")).isNotNull();
    assertThat(userService.findAll()).hasSize(SEEDED_USERS);
  }

  @Test
  void save_blankPasswordThrows() {
    ValidationException ex = assertThrows(ValidationException.class,
        () -> userService.save(user("editor", "", true)));

    assertThat(ex.getBindingResult().getFieldError("password")).isNotNull();
  }

  @Test
  void findById_returnsEmptyWhenNotExists() {
    assertThat(userService.findById(9_999_999L)).isEmpty();
  }

  @Test
  void findAll_returnsSeededRowsPlusTheNewOne() {
    userService.save(user("editor", "secret123", true));

    assertThat(userService.findAll()).hasSize(SEEDED_USERS + 1);
  }

  @Test
  void deleteById_removesTheRow() {
    User saved = userService.save(user("editor", "secret123", true));

    userService.deleteById(saved.getId());

    assertThat(userRepository.findById(saved.getId())).isEmpty();
  }

  private User user(String username, String password, boolean enabled) {
    return new User(null, username, password, enabled);
  }
}
