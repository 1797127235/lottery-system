package org.lotterysystem.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final String issuer;
    private final long expirationMs;

    // 构造器注入：Spring创建Bean时就把配置注入进来，不需要@PostConstruct，也不需要static变量
    public JwtUtil(
            @Value("${jwt.secret}") String secretBase64,
            @Value("${jwt.issuer:demo-api}") String issuer,
            @Value("${jwt.expiration-ms:3600000}") long expirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
        this.issuer = issuer;
        this.expirationMs = expirationMs;
    }

    /** 生成 JWT：建议至少包含 sub/iss/iat/exp */
    public String genJwt(String userId, Map<String, Object> claims) {
        long now = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .issuer(issuer)
                .subject(userId)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key);

        if (claims != null && !claims.isEmpty()) {
            builder.claims(claims);
        }
        return builder.compact();
    }

    /** 解析 + 验签 + 校验过期时间：失败直接抛JwtException（上层统一返回401） */
    public Claims parseJwt(String token) {
        if (token == null || token.isBlank()) {
            throw new JwtException("Empty token");
        }

        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
