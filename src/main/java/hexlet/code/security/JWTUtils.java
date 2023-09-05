package hexlet.code.security;

import hexlet.code.domain.model.User;
import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

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
        final Claims claims = Jwts.claims();
        claims.setIssuer(JWT_ISSUER);
        claims.setSubject(JWT_SUBJECT);
        return claims;



//        return Jwts.parserBuilder()
//                .requireSubject(JWT_SUBJECT)
//                .requireIssuer(JWT_ISSUER)
//                .setAllowedClockSkewSeconds(120)
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
    }
}
