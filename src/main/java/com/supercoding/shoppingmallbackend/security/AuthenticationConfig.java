package com.supercoding.shoppingmallbackend.security;

import com.supercoding.shoppingmallbackend.entity.ProfileRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
@Configuration
public class AuthenticationConfig {
    private final JwtUtiles jwtUtiles;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        return http
                .csrf().disable()
                .cors()
                .and()
                .addFilterBefore(new EndExceptionHandler(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .authorizeHttpRequests(authorization ->
//                        authorization
//                                .antMatchers(GET, "/api/v1/product/*", "/api/v1/reviews/{id}", "/api/v1/questions/{id}").permitAll()
//                                .antMatchers(POST, "/api/v1/product/*","/api/v1/scrapList/**", "/api/v1/reviews/**",
//                                        "/api/v1/questions/**", "/api/v1/shoppingcart/**", "/api/v1/payments/**").authenticated()
//                                .antMatchers(PUT, "/api/v1/product/*","/api/v1/scrapList/**", "/api/v1/reviews/**",
//                                        "/api/v1/questions/**", "/api/v1/shoppingcart/**", "/api/v1/payments/**").authenticated()
//                                .antMatchers(DELETE, "/api/v1/product/*","/api/v1/scrapList/**", "/api/v1/reviews/**",
//                                        "/api/v1/questions/**", "/api/v1/shoppingcart/**", "/api/v1/payments/**").authenticated()
//                                .antMatchers(GET,"/api/v1/token/**").permitAll()
//                                .antMatchers(POST, "/api/v1/token/**").authenticated()
//                                .anyRequest().permitAll()
//                )
                .exceptionHandling().authenticationEntryPoint(new CustomEntryPoint())
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtUtiles), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
