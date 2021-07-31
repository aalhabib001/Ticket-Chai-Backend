package tech.ticketchai.TicketChaiBackend.jwt.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tech.ticketchai.TicketChaiBackend.jwt.security.services.UserPrinciple;
import tech.ticketchai.TicketChaiBackend.jwt.security.uti.JwtUtil;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private JwtUtil jwtUtil;


    @Value("${grokonez.app.jwtSecret}")
    private String jwtSecret;

    @Value("${grokonez.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        Claims claims = Jwts.claims();
        claims.setSubject(userPrincipal.getUsername());

        return Jwts.builder()
                .setSubject((claims.getSubject()))
                .claim("name", userPrincipal.getName())
                .claim("scopes", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000L))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String getUserNameFromJwt(String bearerToken) {
//        System.out.println(bearerToken);
        try {
            bearerToken = bearerToken.split("\\s+")[1];
//            System.out.println(bearerToken);
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(bearerToken)
                    .getBody().getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "There is a problem in JWT Token");
        }
    }

    public String getNameFromJwt(String bearerToken) {
        try {
            bearerToken = bearerToken.split("\\s+")[1];

            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(bearerToken)
                    .getBody().get("name").toString();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "There is a problem in JWT Token");
        }
    }

    public String getRolesFromJwt(String bearerToken) {
        try {
            bearerToken = bearerToken.split("\\s+")[1];

            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(bearerToken)
                    .getBody().get("scopes").toString();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "There is a problem in JWT Token");
        }
    }
}
