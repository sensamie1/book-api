package com.example.book_api.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Data
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "Title is required")
  @Size(max = 100)
  private String title;

  @NotEmpty(message = "Author is required")
  @Size(max = 50)
  private String author;

  @NotEmpty(message = "ISBN is required")
  @Size(max = 50)
  private Double isbn;
}
