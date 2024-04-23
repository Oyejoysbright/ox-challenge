package org.ox.link_generator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expires-in-minutes}")
    private long expirationTime;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String generateToken(Object load) throws JsonProcessingException {
        Date expiryDate = new Date(System.currentTimeMillis() + (expirationTime*60000));
        return Jwts.builder()
                .setClaims(Map.of("data", MAPPER.writeValueAsString(load)))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public <T> T getTokenData(String token, Class<T> clazz) throws JsonProcessingException {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        String data = (String) claims.get("data");
        return MAPPER.readValue(data, clazz);
    }
}
