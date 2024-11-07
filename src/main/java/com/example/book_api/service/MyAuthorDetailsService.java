package com.example.book_api.service;


import com.example.book_api.model.AuthorPrincipal;
import com.example.book_api.model.Author;
import com.example.book_api.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyAuthorDetailsService implements UserDetailsService {
  @Autowired
  private AuthorRepository authorRepository;


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Author author = authorRepository.findByEmail(email);
    if (author == null) {
      System.out.println("Author Not Found");
      throw new UsernameNotFoundException("Author not found");
    }
      
      return new AuthorPrincipal(author);
  }
}