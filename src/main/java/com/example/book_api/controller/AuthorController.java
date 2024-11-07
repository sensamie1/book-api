package com.example.book_api.controller;

import com.example.book_api.model.Author;
import com.example.book_api.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {

    @Autowired
    private AuthorService service;


    @PostMapping("/register")
    public Author register(@RequestBody Author author) {
        return service.register(author);

    }

    @PostMapping("/login")
    public String login(@RequestBody Author author) {

        return service.verify(author);
    }
}