package com.example.book_api.service;

import com.example.book_api.dto.ApiResponse;
import com.example.book_api.exception.AuthenticationFailedException;
import com.example.book_api.model.Author;
import com.example.book_api.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

  @Mock
  private JWTService jwtService;

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private AuthorRepository authorRepository;

  @InjectMocks
  private AuthorService authorService;

  // private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void register_ShouldRegisterAuthorSuccessfully() {
    // Arrange
    Author author = Author.builder()
    .id(1L)
    .firstName("John")
    .lastName("Doe")
    .email("john@example.com")
    .password("password")
    .build();

    when(authorRepository.save(any(Author.class))).thenReturn(author);

    // Act
    ApiResponse<Author> response = authorService.register(author);

    // Assert
    assertTrue(response.isSuccess());
    assertEquals("Signup successful", response.getMessage());
    assertEquals(author, response.getData());
    verify(authorRepository, times(1)).save(author);
  }

  @Test
  void login_ShouldReturnTokenOnSuccessfulLogin() {
    // Arrange
    Author author = Author.builder()
    .id(1L)
    .firstName("John")
    .lastName("Doe")
    .email("john@example.com")
    .password("password")
    .build();

    String token = "mocked-jwt-token";
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(jwtService.generateToken(author.getEmail())).thenReturn(token);

    // Act
    ApiResponse<String> response = authorService.verify(author);

    // Assert
    assertTrue(response.isSuccess());
    assertEquals("Login successful", response.getMessage());
    assertEquals(token, response.getData());
  }

  @Test
  void login_ShouldThrowAuthenticationFailedException() {
    // Arrange
    Author author = Author.builder()
    .id(1L)
    .firstName("John")
    .lastName("Doe")
    .email("john@example.com")
    .password("wrongperson")
    .build();

    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationFailedException("Email or Password incorrect."));

    // Act & Assert
    AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class, () -> {
      authorService.verify(author);
    });
    assertEquals("Email or Password incorrect.", exception.getMessage());
  }
}
