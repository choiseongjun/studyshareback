package study.share.com.source.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import study.share.com.source.model.AuthTokenType;
import study.share.com.source.model.exception.TokenAuthenticationException;
import study.share.com.source.security.services.UserPrinciple;

import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    
    private String jwtSecret="JWTSuperSecretKey";

    private int jwtExpiration = 864000;

    private static String JWT_TOKEN_TYPE_KEY = "token_type";

    public String generateJwtToken(Authentication authentication) {
    	
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
		                .setSubject((userPrincipal.getUsername()))
		                .setIssuedAt(new Date())
		                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
		                .signWith(SignatureAlgorithm.HS512, jwtSecret)
		                .compact();
    }


    public String generateJwtToken(Long id, String userid, AuthTokenType tokenType) {

        Claims claims = new DefaultClaims()
                .setExpiration(new Date(System.currentTimeMillis() + tokenType.getExpiresIn() * 1000))
                .setId(String.valueOf(id))
                .setIssuedAt(new Date())
                .setSubject(userid);
        claims.put(JWT_TOKEN_TYPE_KEY, tokenType.name());

        return Jwts.builder()
                .setClaims(claims)
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

    public Long getTokenId(String token) {
        return Long.valueOf(Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getId());
    }

    public AuthTokenType getTokenType(String token) {
        return AuthTokenType.convert((String)Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().get(JWT_TOKEN_TYPE_KEY));
    }

    public void assertAuthToken(String token, AuthTokenType tokenType) throws TokenAuthenticationException {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            // 만료 되었다면,
            if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
                throw new TokenAuthenticationException();
            }

        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
            throw new TokenAuthenticationException();
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
            throw new TokenAuthenticationException();
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
            throw new TokenAuthenticationException();
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
            throw new TokenAuthenticationException();
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
            throw new TokenAuthenticationException();
        }

    }
}