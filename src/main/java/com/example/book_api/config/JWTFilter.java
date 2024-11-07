package com.example.book_api.config;

import com.example.book_api.service.JWTService;
import com.example.book_api.service.MyAuthorDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraWxsIiwiaWF0IjoxNzIzMTgzNzExLCJleHAiOjE3MjMxODM4MTl9.5nf7dRzKRiuGurN2B9dHh_M5xiu73ZzWPr6rbhOTTHs
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtService.extractUserName(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyAuthorDetailsService.class).loadUserByUsername(email);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}






// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;

// @Component
// public class JwtRequestFilter extends OncePerRequestFilter {

//   @Autowired
//   private JwtUtil jwtUtil;

//   @Autowired
//   private UserDetailsService userDetailsService;

//   @Override
//   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//       throws ServletException, IOException {

//     final String authorizationHeader = request.getHeader("Authorization");

//     String email = null;
//     String jwt = null;

//     // Extract token from Authorization header
//     if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//       jwt = authorizationHeader.substring(7);
//       email = jwtUtil.extractEmail(jwt); // Extract the email from the token
//     }

//     // Validate the token and set authentication context if valid
//     if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//       UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

//       if (jwtUtil.validateToken(jwt)) {
//         UsernamePasswordAuthenticationToken authenticationToken =
//           new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//         authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//       }
//     }

//     chain.doFilter(request, response);
//   }
// }
