package com.hotel.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
        "pG5+XlP2Fs1dzA5mV3QhKa8Lw7TqY35785899tJcRfNpMvBdU6Zoghdfhg76576fjh7657865hjgrhjXgK9HrWyEjC2Vu0IbOuyryuryu4387ergfhueruyr##$$DFDFERR^$dvhjefjhfheyur78347843jhhejjhf"
        .getBytes()
    );

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token.replace("Bearer ", ""));
            return true;
        } catch (Exception e) {
            System.err.println("JWT Validation Failed: " + e.getMessage());
            return false;
        }
    }

    public String extractRole(String token) {
        Claims claims = (Claims) Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parse(token.replace("Bearer ", ""))
                .getPayload();

        return claims.get("role", String.class);
    }
}
