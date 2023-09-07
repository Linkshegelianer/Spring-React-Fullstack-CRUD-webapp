package hexlet.code.security;

import hexlet.code.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

import static io.jsonwebtoken.impl.TextCodec.BASE64;


@Component
public class JWTUtils {

    private static final String JWT_SUBJECT = "user-details";
    private static final String JWT_ISSUER = "spring-app";
    private final String key;

    public JWTUtils(@Value("${jwt.secret}") String secret) {
        this.key = BASE64.encode(secret);
    }

    public String generateToken(User user) {
        ZonedDateTime systemTimestamp = ZonedDateTime.now();
        Date creationDate = Date.from(systemTimestamp.toInstant());
        Date expirationDate = Date.from(systemTimestamp.plusHours(24).toInstant());

        return Jwts.builder()
                .setSubject(JWT_SUBJECT)
                .setIssuer(JWT_ISSUER)
                .claim("email", user.getEmail())
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Claims getClaims(String token) throws JwtException {
        return Jwts.parser()
                .requireSubject(JWT_SUBJECT)
                .requireIssuer(JWT_ISSUER)
                .setAllowedClockSkewSeconds(120)
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }
}
