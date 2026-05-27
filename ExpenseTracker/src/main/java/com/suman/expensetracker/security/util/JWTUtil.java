package com.suman.expensetracker.security.util;

import com.suman.expensetracker.constants.ApplicationConstant;
import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final Environment env;

    public String generateJwtToekn(Authentication authentication) {
        String jwtToken;
        String secret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY,
                ApplicationConstant.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String email = authentication.getName();
        jwtToken = Jwts.builder().issuer("Expense Tracker").subject("JWT Token")
                .claim("email", email)
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date((new java.util.Date()).getTime() + 1 * 60 * 60 * 1000))
                .signWith(secretKey).compact();
        return jwtToken;
    }
}
