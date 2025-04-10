package com.usercard.manager.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class Configurations {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200/", "http://127.0.0.1:4200/")); // Use allowedOriginPatterns instead of allowedOrigins
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Accept", "X-Requested-With", "Content-Type",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomTokenAuthFilter customTokenAuthFilter) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).disable())
                .headers(headers -> headers
                        .frameOptions(FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(req -> {

                    //Endpoints de Usuário
                    req.requestMatchers(HttpMethod.GET,"/usuario/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/usuario/**").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/usuario/**").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/usuario/**").authenticated();

                    //Endpoints de Cartão
                    req.requestMatchers(HttpMethod.GET,"/cartao/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/cartao/**").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/cartao/**").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/cartao/**").authenticated();

                    //Endpoint de testes do Swagger
                    req.requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.PUT, "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.DELETE, "/swagger-ui/**").permitAll();
                })
                .addFilterBefore(customTokenAuthFilter, AuthenticationFilter.class)
                .build();
    }

}
