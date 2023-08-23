package hexlet.code.security;

import hexlet.code.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtils {

    private static final String JWT_SUBJECT = "user-details";
    private static final String JWT_ISSUER = "spring-app";
    private static final long CLOCK_SKEW = 3 * 60; // 3 minutes
    // BASE64-encoded algorithm-specific signing key to use to digitally sign the JWT
    private final SecretKey key;

    public JWTUtils(@Value("${jwt.secret}") String base64EncodedString) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64EncodedString));
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
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims validateAndRetrieveClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
            .requireSubject(JWT_SUBJECT)
            .requireIssuer(JWT_ISSUER)
            .setAllowedClockSkewSeconds(CLOCK_SKEW)
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /*public boolean isValid(String token) {
        return getEmail(token) != null && !isExpired(token);
    }

    public String getEmail(String token) {
        Claims claims = validateAndRetrieveClaims(token);
        return claims.get("email", String.class);
    }

    public boolean isExpired(String token) {
        ZonedDateTime systemTimestamp = ZonedDateTime.now();
        Claims claims = validateAndRetrieveClaims(token);
        return claims.getExpiration().after(Date.from(systemTimestamp.toInstant()));
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setAllowedClockSkewSeconds(CLOCK_SKEW)
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }*/
}
