package com.example.book_api.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

@Entity
@Data
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "First name is mandatory")
  @Size(max = 50)
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @NotBlank(message = "Last name is mandatory")
  @Size(max = 50)
  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  @Size(max = 100)
  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @NotEmpty(message = "Password is required")
  private String password;
}
