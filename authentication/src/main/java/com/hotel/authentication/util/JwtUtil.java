package com.hotel.authentication.util;

import com.hotel.authentication.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String secret = "pG5+XlP2Fs1dzA5mV3QhKa8Lw7TqY35785899tJcRfNpMvBdU6Zoghdfhg76576fjh7657865hjgrhjXgK9HrWyEjC2Vu0IbOuyryuryu4387ergfhueruyr##$$DFDFERR^$dvhjefjhfheyur78347843jhhejjhf";

    private final long expiration = 86400000L; // 1 day

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }
}
