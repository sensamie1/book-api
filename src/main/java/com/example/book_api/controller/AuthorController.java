package com.example.book_api.controller;

import com.example.book_api.dto.ApiResponse;
import com.example.book_api.model.Author;
import com.example.book_api.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // Register a new author
    @PostMapping("/register")
    public ApiResponse<Author> register(@RequestBody Author author) {
        return authorService.register(author);
    }

    // Login an author
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody Author author) {
        return authorService.verify(author);
    }
}
