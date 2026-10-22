package com.portfolio.my_portfolio.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
  private final BindingResult bindingResult;

  public ValidationException(BindingResult bindingResult) {
    super("Error de validación: Se encontraron " + bindingResult.getErrorCount());
    this.bindingResult = bindingResult;
  }
}
