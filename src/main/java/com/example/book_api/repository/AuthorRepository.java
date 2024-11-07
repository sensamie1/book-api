package com.example.book_api.repository;

import com.example.book_api.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
// import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
  Author findByEmail(String email);
}
