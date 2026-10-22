package com.portfolio.my_portfolio.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.my_portfolio.exception.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleValidationException(ValidationException ex) {
    ModelAndView mav = new ModelAndView("errors/validation");
    if (ex.getBindingResult() != null) {
      mav.addObject("errors", ex.getBindingResult().getAllErrors());
    }
    mav.addObject("message", "Se encontraron errores de validación");
    return mav;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    ModelAndView mav = new ModelAndView("errors/validation");
    mav.addObject("errors", ex.getBindingResult().getAllErrors());
    mav.addObject("message", "Se encontraron errores de validación");
    return mav;
  }
}
