package com.example.book_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {


    private String secretkey = "";

    public JWTService() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 24))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username (email) from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}









// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import java.security.Key;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.function.Function;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;

// @Service
// public class JwtService {
//     @Value("${jwt.secret}")
//     private String secretKey;

//     private final long expiration = 86400000; // 24 hours in milliseconds;

//     public String extractUsername(String token) {
//         return extractClaim(token, Claims::getSubject);
//     }

//     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = extractAllClaims(token);
//         return claimsResolver.apply(claims);
//     }

//     public String generateToken(UserDetails userDetails) {
//         return generateToken(new HashMap<>(), userDetails);
//     }

//     public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//         return buildToken(extraClaims, userDetails, expiration);
//     }i

//     public long getExpirationTime() {
//         return expiration;
//     }

//     private String buildToken(
//             Map<String, Object> extraClaims,
//             UserDetails userDetails,
//             long expiration
//     ) {
//         return Jwts
//                 .builder()
//                 .claims(extraClaims)
//                 .subject(userDetails.getUsername())
//                 .issuedAt(new Date(System.currentTimeMillis()))
//                 .expiration(new Date(System.currentTimeMillis() + expiration))
//                 // .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                 // .signWith(secretKey, SignatureAlgorithm)
//                 // .signWith(secretKey, Jwts.SIG.HS256)
//                 // .signWith(getSignInKey(), Jwts.SIG.HS256)
//                 // .sign(secretKey, SecureDigestAlgorithm) 
//                 .compact();
//     }

//     public boolean isTokenValid(String token, UserDetails userDetails) {
//         final String username = extractUsername(token);
//         return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//     }

//     private boolean isTokenExpired(String token) {
//         return extractExpiration(token).before(new Date());
//     }

//     private Date extractExpiration(String token) {
//         return extractClaim(token, Claims::getExpiration);
//     }

//     private Claims extractAllClaims(String token) {
//         return Jwts
//                 .parserBuilder()
//                 .setSigningKey(getSignInKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }

//     private Key getSignInKey() {
//         byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//         return Keys.hmacShaKeyFor(keyBytes);
//     }
// }




// import io.jsonwebtoken.*;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;

// import javax.crypto.SecretKey;

// @Component
// public class JwtUtil {

//   @Value("${jwt.secret}")
//   private String secret;
  
//   private final long expiration = 86400000; // 24 hours in milliseconds

//   // Generate a JWT token with firstName, lastName, and email as claims
//   public String generateToken(String firstName, String lastName, String email) {
//     Map<String, Object> claims = new HashMap<>();
//     claims.put("firstName", firstName);
//     claims.put("lastName", lastName);
//     claims.put("email", email);

//     return Jwts.builder()
//       .claims(claims)
//       .subject(email) // Using email as the subject
//       .issuedAt(new Date())
//       .expiration(new Date(System.currentTimeMillis() + expiration))
//       .signWith(getSignInKey(), Jwts.SIG.HS256)
//       .compact();
//   }
//   private Claims getClaimFromToken(String token) {
//     return Jwts.parser()
//       .verifyWith(getSignInKey())
//       .build()
//       .parseSignedClaims(token)
//       .getPayload();
// }

//     // Extract the email (used as username/subject) from the JWT token
//     public String extractEmail(String token) {
//       return getClaimFromToken(token, Claims::getSubject);
//     }

//     // Extract the first name from the JWT token
//     public String extractFirstName(String token) {
//         return (String) getClaimFromToken(token, claims -> claims.get("firstName"));
//     }

//     // Extract the last name from the JWT token
//     public String extractLastName(String token) {
//         return (String) getClaimFromToken(token, claims -> claims.get("lastName"));
//     }

//     private SecretKey getSignInKey() {
//       byte[] keyBytes = Decoders.BASE64.decode(secret);
//       return Keys.hmacShaKeyFor(keyBytes);
//     }

//     // Validate the token by checking expiration and signature
//     public boolean validateToken(String token) {
//         try {
//             Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//             return true;
//         } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
//             System.out.println("Invalid JWT token: " + e.getMessage());
//             return false;
//         }
//     }

//     // Helper method to retrieve claims from token
//     private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//         return claimsResolver.apply(claims);
//     }
// }
