package tr.edu.ku.ulgen.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A service class responsible for managing JSON Web Tokens (JWT).
 * It provides methods to generate, validate, and extract information from JWT tokens.
 *
 * @author Kaan Turkmen
 */
@Service
public class JwtService {

    @Value("${SECRET_SIGN_KEY_ULGEN}")
    private String SECRET_KEY = "2F413F4428472B4B6150645367566B5970337336763979244226452948404D63";

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token.
     * @return the username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the given JWT token using the provided claims resolver.
     *
     * @param token          the JWT token.
     * @param claimsResolver a function to resolve a specific claim from the token.
     * @param <T>            the type of the claim value.
     * @return the claim value of the specified type.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details for which the JWT token should be generated.
     * @return the generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with extra claims for the given user details.
     *
     * @param extraClaims a map of extra claims to be added to the JWT token.
     * @param userDetails the user details for which the JWT token should be generated.
     * @return the generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 6))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if the given JWT token is valid for the provided user details.
     *
     * @param token       the JWT token.
     * @param userDetails the user details to validate the token against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the provided token contains a specific claim.
     *
     * @param token     the JWT token to be checked for the presence of the claim.
     * @param claimName the name of the claim to look for in the token.
     * @return true if the token contains the specified claim, false otherwise.
     */
    public boolean hasClaim(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return claims.containsKey(claimName);
    }

    /**
     * Checks if the given JWT token has expired.
     *
     * @param token the JWT token.
     * @return true if the token has expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token.
     * @return the expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token.
     * @return the claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the signing key used for JWT token signature and verification.
     *
     * @return the signing key as a java.security.Key object.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
