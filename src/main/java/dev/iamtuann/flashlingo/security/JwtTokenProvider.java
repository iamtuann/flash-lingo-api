package dev.iamtuann.flashlingo.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret-key}")
    private String jwtSecret;

    @Value("${app.jwt.expire-length}")
    private long jwtExpire;

    public String generateToken(Authentication authentication){

        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpire);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
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
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            // throw exception
        } catch (MalformedJwtException e) {
            // throw exception
        } catch (ExpiredJwtException e) {
            // throw exception
        } catch (UnsupportedJwtException e) {
            // throw exception
        } catch (IllegalArgumentException e) {
            // throw exception
        }

        return false;
    }
}
