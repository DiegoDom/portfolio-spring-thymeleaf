package com.portfolio.my_portfolio.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.my_portfolio.exception.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public Object handleValidationException(ValidationException ex, HttpServletRequest request) {
    if (ex.getBindingResult() != null && acceptsHtml(request)) {
      ModelAndView mav = new ModelAndView("errors/validation");
      mav.addObject("errors", ex.getBindingResult().getAllErrors());
      mav.addObject("message", "Se encontraron errores de validación");
      mav.setStatus(HttpStatus.BAD_REQUEST);
      return mav;
    }

    Map<String, Object> body = new HashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Validation Failed");
    body.put("message", ex.getMessage());
    if (ex.getBindingResult() != null) {
      body.put("errors", ex.getBindingResult().getAllErrors().stream()
          .map(err -> Map.of(
              "field",
              err instanceof org.springframework.validation.FieldError fe ? fe.getField() : err.getObjectName(),
              "message", err.getDefaultMessage()))
          .collect(Collectors.toList()));
    }
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
  public Object handleValidationExceptions(Exception ex, HttpServletRequest request) {
    var bindingResult = ex instanceof MethodArgumentNotValidException manve ? manve.getBindingResult()
        : ex instanceof BindException be ? be.getBindingResult()
            : null;

    if (bindingResult != null && acceptsHtml(request)) {
      ModelAndView mav = new ModelAndView("errors/validation");
      mav.addObject("errors", bindingResult.getAllErrors());
      mav.addObject("message", "Se encontraron errores de validación");
      mav.setStatus(HttpStatus.BAD_REQUEST);
      return mav;
    }

    Map<String, Object> body = new HashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Validation Failed");
    if (bindingResult != null) {
      body.put("errors", bindingResult.getAllErrors().stream()
          .map(err -> {
            Map<String, String> map = new HashMap<>();
            if (err instanceof org.springframework.validation.FieldError fe) {
              map.put("field", fe.getField());
            } else {
              map.put("object", err.getObjectName());
            }
            map.put("message", err.getDefaultMessage());
            return map;
          })
          .collect(Collectors.toList()));
    }
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  private boolean acceptsHtml(HttpServletRequest request) {
    String accept = request.getHeader("Accept");
    if (accept == null) {
      return false;
    }
    return accept.contains(MediaType.TEXT_HTML_VALUE) && !accept.contains(MediaType.APPLICATION_JSON_VALUE);
  }
}
