package com.example.book_api.handler;

import com.example.book_api.dto.ApiResponse;
import com.example.book_api.exception.AuthenticationFailedException;
import com.example.book_api.exception.BookNotFoundException;
import com.example.book_api.exception.NoBooksAvailableException;
import com.example.book_api.exception.DuplicateIsbnException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BookNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponse<?> handleBookNotFoundException(BookNotFoundException ex) {
    return new ApiResponse<>(false, ex.getMessage(), null);
  }

  @ExceptionHandler(NoBooksAvailableException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponse<?> handleNoBooksAvailableException(NoBooksAvailableException ex) {
    return new ApiResponse<>(false, ex.getMessage(), null);
  }

  @ExceptionHandler(DuplicateIsbnException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiResponse<?> handleDuplicateIsbnException(DuplicateIsbnException ex) {
    return new ApiResponse<>(false, ex.getMessage(), null);
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResponse<?> handleAuthenticationFailedException(AuthenticationFailedException ex) {
    return new ApiResponse<>(false, ex.getMessage(), null);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<?> handleGenericException(Exception ex) {
    return new ApiResponse<>(false, "An unexpected error occurred", null);
  }
}
