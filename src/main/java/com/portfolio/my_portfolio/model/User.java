package com.portfolio.my_portfolio.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Usuario del panel de administración.
 *
 * Implementa UserDetails porque los datos de autenticación (usuario,
 * contraseña, estado) son de cada usuario, no del servicio: UserServiceImpl es
 * un singleton y no puede guardar el usuario autenticado en sus campos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
  private Long id;

  @NotBlank(message = "El usuario no puede estar vacío")
  @Size(max = 50, message = "El usuario no puede superar los 50 caracteres")
  private String username;

  @NotBlank(message = "La contraseña no puede estar vacía")
  @Size(max = 255, message = "La contraseña no puede superar los 255 caracteres")
  @ToString.Exclude
  private String password;

  private boolean enabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // users no tiene columna role: hasta que exista, todo usuario es ADMIN.
    return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
