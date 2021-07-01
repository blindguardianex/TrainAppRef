package com.smartru.receiver.configuration.security.jwt;

import com.smartru.common.entity.User;
import com.smartru.common.service.jpa.UserService;
import com.smartru.common.service.redis.TokenService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Класс, создающий и работающий с Jwt токенами
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${token.access.secret}")
    private String secret;
    @Value("${token.access.expired}")
    private long sessionTime;

    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public JwtTokenProvider(@Qualifier("redisTokenServiceJedisImpl") TokenService tokenService,
                            UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostConstruct
    void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }


    public Map<Object, Object> createTokens(User user){
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(accessToken);
        addTokensToUser(user,accessToken,refreshToken);

        Map<Object, Object> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        return tokenMap;
    }

    public Map<Object,Object> refreshTokens(String refreshToken, String accessToken){
        if(validateRefreshToken(refreshToken,accessToken)){
            accessToken = accessToken.substring(7);
            String username = getLogin(accessToken);
            User user = userService.getByUsername(username).orElseThrow(()->{
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            });
            if (refreshToken.equals(user.getRefreshToken())) {
                return createTokens(user);
            }
        }
        throw new JwtException("Incorrect refresh token");
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = tokenToUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getLogin(String token){
        try{
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException ex){
            return ex.getClaims().getSubject();
        }
    }

    public String getRole(String token){
        try{
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("role", String.class);
        } catch (ExpiredJwtException ex){
            return ex.getClaims().getSubject();
        }
    }

    public String tokenFromRequest(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken!=null&&bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateAccessToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                log.warn("Token is expired!");
                return false;
            }
            return checkTokenInRedis(token);
        }catch (JedisConnectionException ex){
            log.error("Redis server does not respond! Check token in mysql db");
            return checkTokenInDatabase(token);
        } catch (JwtException | IllegalArgumentException ex){
            log.warn("JWT token is expired or invalid");
            return false;
        }
    }

    private String createAccessToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("id",user.getId());
        claims.put("role", user.getRole());

        Date now = new Date();
        Date validateDate = new Date(now.getTime()+ sessionTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validateDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private String createRefreshToken(String token){
        String tokenTail = token.substring(token.length()-8);
        String randomString = UUID.randomUUID().toString();
        return tokenTail+"_"+randomString;
    }

    private UserDetails tokenToUserDetails(String token){
        String login = getLogin(token);
        String role = getRole(token);
        return org.springframework.security.core.userdetails.User.builder()
                .username(login)
                .password("")
                .authorities(role)
                .build();
    }

    private boolean checkTokenInRedis(String token){
            if (!tokenService.tokenExists(token)) {
                log.error("Access token is not found");
                return false;
            }
            return true;
    }

    private boolean checkTokenInDatabase(String token){
        String username = getLogin(token);
        User user = userService.getByUsername(username).orElseThrow(()->{
            throw new UsernameNotFoundException("User with username: " + username + " not found. Token is broken");
        });
        return token.equals(user.getAccessToken());
    }

    private void addTokensToUser(final User user, String accessToken, String refreshToken){
        try {
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userService.updateTokens(user);

            tokenService.addToken(String.valueOf(user.getId()), accessToken);
        } catch (JedisConnectionException e){
            log.error("Server redis does not respond! Saving only mysql db");
        }
    }

    private boolean validateRefreshToken(String refreshToken, String accessToken){
        return refreshToken.startsWith(accessToken.substring(accessToken.length()-8));
    }
}
