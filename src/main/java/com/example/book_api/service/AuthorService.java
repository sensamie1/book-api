package com.example.book_api.service;

import com.example.book_api.dto.ApiResponse;
import com.example.book_api.exception.AuthenticationFailedException;
import com.example.book_api.model.Author;
import com.example.book_api.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthorRepository authorRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Register a new author
    public ApiResponse<Author> register(Author author) {
        author.setPassword(encoder.encode(author.getPassword()));
        Author savedAuthor = authorRepository.save(author);
        return new ApiResponse<>(true, "Signup successful", savedAuthor);
    }

    // Verify author login credentials
    public ApiResponse<String> verify(Author author) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(author.getEmail(), author.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(author.getEmail());
                return new ApiResponse<>(true, "Login successful", token);
            } else {
                throw new AuthenticationFailedException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new AuthenticationFailedException("Email or Password incorrect.");
        }
    }
}






// import com.example.book_api.model.Author;
// import com.example.book_api.repository.AuthorRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class AuthorService {

//   @Autowired
//   private AuthorRepository authorRepository;

//   @Autowired
//   private PasswordEncoder passwordEncoder;

//   public Author register(Author author) {
//     author.setPassword(passwordEncoder.encode(author.getPassword()));
//     return authorRepository.save(author);
//   }

//   public Author authenticate(String email, String password) {
//     Author author = authorRepository.findByUsername(email)
//       .orElseThrow(() -> new RuntimeException("Author not found"));

//     if (passwordEncoder.matches(password, author.getPassword())) {
//       return author;
//     } else {
//       throw new RuntimeException("Email or Password incorrect.");
//     }
//   }
// }
