package dev.us00ng.week1_login.config.security;

import dev.us00ng.week1_login.config.security.jwt.JwtFilter;
import dev.us00ng.week1_login.domain.oauth.OAuth2AuthenticationSuccessHandler;
import dev.us00ng.week1_login.domain.oauth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2Service oAuth2Service;
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000"); // 허용할 출처
        config.addAllowedOrigin("http://localhost:8080"); // 허용할 출처
        config.addAllowedMethod("*"); // 허용할 메서드 (GET, POST, PUT 등)
        config.addAllowedHeader("*"); // 허용할 헤더
        config.setAllowCredentials(true); // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .formLogin((f) -> f.disable())
                .authorizeHttpRequests((auth) ->
                    auth
                        .requestMatchers("/member/**", "/oauth2/**").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement((session) -> {
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                        session.sessionFixation().none();})
                .oauth2Login((config) -> {
                    config.successHandler(oAuth2AuthenticationSuccessHandler);
                    config.userInfoEndpoint((endpoint) -> endpoint.userService(oAuth2Service));
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
