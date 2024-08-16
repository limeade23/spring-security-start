package example.jwtstart.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private SecretKey secretKey;

    //@Value("${jwt.access-token.expiration}")
    private final long ACCESS_TOKEN_TIME = 10 * 60 * 1000L; // 10분
    //@Value("${jwt.refresh-token.expiration}")
    private final long REFRESH_TOKEN_TIME = 2 * 24 * 60 * 60 * 1000L; // 2일

    public JwtProvider(@Value("${jwt.key}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("userId", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("role", String.class);

    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .getExpiration().before(new Date());
    }

    public String createToken(String userId, String role, long expiration) {
        Date now = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
            .claim("userId", userId)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(secretKey)
            .compact();
    }

}
