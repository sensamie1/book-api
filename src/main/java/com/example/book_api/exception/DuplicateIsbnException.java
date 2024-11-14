package com.example.book_api.exception;

public class DuplicateIsbnException extends RuntimeException {
  public DuplicateIsbnException(String message) {
    super(message);
  }
}
