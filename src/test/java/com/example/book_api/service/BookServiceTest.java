package com.example.book_api.service;

import com.example.book_api.dto.ApiResponse;
import com.example.book_api.exception.DuplicateIsbnException;
import com.example.book_api.model.Book;
import com.example.book_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void addBook_ShouldAddBookSuccessfully() {
    // Arrange
    Book book = Book.builder()
    .id(1L)
    .title("Test Book")
    .author("John Doe")
    .isbn("1234567890")
    .build();
;
    when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());
    when(bookRepository.save(any(Book.class))).thenReturn(book);

    // Act
    ApiResponse<Book> response = bookService.addBook(book);

    // Assert
    assertTrue(response.isSuccess());
    assertEquals("Book added successfully", response.getMessage());
    assertEquals(book, response.getData());
    verify(bookRepository, times(1)).save(book);
  }

  @Test
  void addBook_ShouldThrowDuplicateIsbnException() {
    // Arrange
    Book book = Book.builder()
    .id(1L)
    .title("Test Book")
    .author("John Doe")
    .isbn("1234567890")
    .build();
    when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

    // Act & Assert
    DuplicateIsbnException exception = assertThrows(DuplicateIsbnException.class, () -> {
      bookService.addBook(book);
    });
    assertEquals("A book with ISBN 1234567890 already exists.", exception.getMessage());
    verify(bookRepository, never()).save(book);
  }
}
