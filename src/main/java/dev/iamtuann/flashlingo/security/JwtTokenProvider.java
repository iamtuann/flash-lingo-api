package dev.iamtuann.flashlingo.security;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.exception.APIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret-key}")
    private String jwtSecret;

    @Value("${app.jwt.expire-length}")
    private long jwtExpire;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpire);

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    public String generateToken(AuthUser authUser){

        String email = authUser.getEmail();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpire);

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String getUsername(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Jwt claims string is null or empty");
        }
    }
}
