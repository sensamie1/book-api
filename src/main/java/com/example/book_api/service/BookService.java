package com.example.book_api.service;

import com.example.book_api.dto.ApiResponse;
import com.example.book_api.exception.BookNotFoundException;
import com.example.book_api.exception.DuplicateIsbnException;
import com.example.book_api.exception.NoBooksAvailableException;
import com.example.book_api.model.Book;
import com.example.book_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  // Get all books
  public ApiResponse<List<Book>> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    if (books.isEmpty()) {
      throw new NoBooksAvailableException("No books available in the library");
    }
    return new ApiResponse<>(true, "Books fetched successfully", books);
  }

  // Get a book by ID
  public ApiResponse<Book> getBookById(Long id) {
    Book book = bookRepository.findById(id)
      .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    return new ApiResponse<>(true, "Book found", book);
  }

    // Add a new book
    public ApiResponse<Book> addBook(Book book) {
      // Check if a book with the same ISBN already exists
      Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
      if (existingBook.isPresent()) {
          throw new DuplicateIsbnException("A book with ISBN " + book.getIsbn() + " already exists.");
      }

      Book savedBook = bookRepository.save(book);
      return new ApiResponse<>(true, "Book added successfully", savedBook);
    }

  // Update a book by ID
  public ApiResponse<Book> updateBook(Long id, Book updatedBook) {
    Book book = bookRepository.findById(id)
      .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    book.setTitle(updatedBook.getTitle());
    book.setAuthor(updatedBook.getAuthor());
    book.setIsbn(updatedBook.getIsbn());
    Book savedBook = bookRepository.save(book);
    return new ApiResponse<>(true, "Book updated successfully", savedBook);
  }

  // Delete a book by ID
  public ApiResponse<String> deleteBook(Long id) {
    if (!bookRepository.existsById(id)) {
      throw new BookNotFoundException("Book not found with id " + id);
    }
    bookRepository.deleteById(id);
    return new ApiResponse<>(true, "Book deleted successfully", null);
  }

  // Search books by title or author
  public ApiResponse<List<Book>> searchBooks(String query) {
    List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    if (books.isEmpty()) {
      throw new NoBooksAvailableException("No books found matching the query: " + query);
    }
    return new ApiResponse<>(true, "Books found", books);
  }
}
