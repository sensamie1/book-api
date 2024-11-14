package com.example.book_api.exception;

public class NoBooksAvailableException extends RuntimeException {
  public NoBooksAvailableException(String message) {
    super(message);
  }
}
