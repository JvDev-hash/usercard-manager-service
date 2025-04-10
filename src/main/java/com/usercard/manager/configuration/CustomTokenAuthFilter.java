package com.usercard.manager.configuration;

import com.auth0.jwt.interfaces.Claim;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomTokenAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var headerCookies = request.getCookies();
        if(headerCookies == null){
            filterChain.doFilter(request, response);
            return;
        }
        var tokenCookie = this.readCookie(request, "token")
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with this key"));
        var method = request.getMethod();
        var userPermission = this.getClaimFromToken(tokenCookie).get("permissao").asString();

        Logger logger = LoggerFactory.getLogger(CustomTokenAuthFilter.class);

        if(verifyUserPermissionByMethod(method, userPermission)){
            var authToken = new BearerTokenAuthenticationToken(tokenCookie);
            authToken.setAuthenticated(true);
            logger.info("User authenticated");
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);

    }

    private Optional<String> readCookie(HttpServletRequest request, String key) {
        return Arrays.stream(request.getCookies())
                .filter(c -> key.equals(c.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

    private Map<String, Claim> getClaimFromToken(String token) {
        try {
            Dotenv dotenv = Dotenv.load();

            var SECRET_KEY = dotenv.get("SECRET_KEY");

            var ISSUER = dotenv.get("ISSUER");
            // Define o algoritmo HMAC SHA256 para verificar a assinatura do token passando a chave secreta definida
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER) // Define o emissor do token
                    .build()
                    .verify(token) // Verifica a validade do token
                    .getClaims(); // Obtém o assunto (neste caso, o nome de usuário) do token
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    private Boolean verifyUserPermissionByMethod(String method, String permission){
        return switch (method) {
            case "POST", "PUT" -> !permission.equals("VIEW");
            case "DELETE" -> permission.equals("ADMIN");
            default -> false;
        };
    }
}
