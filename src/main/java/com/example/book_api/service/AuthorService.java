package com.example.book_api.service;

import com.example.book_api.model.Author;
import com.example.book_api.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Author register(Author author) {
    author.setPassword(passwordEncoder.encode(author.getPassword()));
    return authorRepository.save(author);
  }

  public Author authenticate(String email, String password) {
    Author author = authorRepository.findByUsername(email)
      .orElseThrow(() -> new RuntimeException("Author not found"));

    if (passwordEncoder.matches(password, author.getPassword())) {
      return author;
    } else {
      throw new RuntimeException("Email or Password incorrect.");
    }
  }
}
