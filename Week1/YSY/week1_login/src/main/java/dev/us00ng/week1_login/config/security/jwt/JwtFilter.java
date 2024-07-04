package dev.us00ng.week1_login.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> token = resolveToken(request);
        if (token.isPresent()) {
            setAuthentication(token.get());
        }

        filterChain.doFilter(request,response);
    }

    private void setAuthentication(String token) {
        if (tokenProvider.validateToken(token)) {
            //TODO: 제대로된 authentication 인지 검증 확인 필요
           Authentication authentication = tokenProvider.getAuthentication(token);
           SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }


    private Optional<String> resolveToken(final HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }

        return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
    }
}
