package com.example.vueblog.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "veublog.jwt")
public class JwtUtils {

    private String secret;
    private Long expire;
    private String header;

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire * 1000);

        Algorithm algorithm = Algorithm.HMAC512(secret);
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("typ", "JWT");
        headerClaims.putIfAbsent("alo", "HS512");
        return JWT.create()
                .withHeader(headerClaims)
                .withSubject(userId.toString())
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public Map<String, Claim> getClaimsByToken(String token) {
        return JWT.decode(token).getClaims();
    }

    public Long getUserIdByToken(String token) {

        return Long.valueOf(JWT.decode(token).getSubject());
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
