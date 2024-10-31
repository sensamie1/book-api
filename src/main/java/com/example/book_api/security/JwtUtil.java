package com.example.book_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;
  
  private final long expiration = 86400000; // 24 hours in milliseconds

  // Generate a JWT token with firstName, lastName, and email as claims
  public String generateToken(String firstName, String lastName, String email) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("firstName", firstName);
    claims.put("lastName", lastName);
    claims.put("email", email);

    return Jwts.builder()
      .claims(claims)
      .subject(email) // Using email as the subject
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSignInKey(), Jwts.SIG.HS256)
      .compact();
  }
  private Claims getClaimFromToken(String token) {
    return Jwts.parser()
      .verifyWith(getSignInKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
}

    // Extract the email (used as username/subject) from the JWT token
    public String extractEmail(String token) {
      return getClaimFromToken(token, Claims::getSubject);
    }

    // Extract the first name from the JWT token
    public String extractFirstName(String token) {
        return (String) getClaimFromToken(token, claims -> claims.get("firstName"));
    }

    // Extract the last name from the JWT token
    public String extractLastName(String token) {
        return (String) getClaimFromToken(token, claims -> claims.get("lastName"));
    }

    private SecretKey getSignInKey() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validate the token by checking expiration and signature
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    // Helper method to retrieve claims from token
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}
