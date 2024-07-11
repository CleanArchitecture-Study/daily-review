package org.example.oauth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.oauth.jwt.RefreshTokenRepository;
import org.example.oauth.jwt.model.RefreshToken;
import org.example.oauth.member.model.CustomUserDetails;
import org.example.oauth.member.model.Member;
import org.example.oauth.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")){
            System.out.println("Bearer 토큰이 없음");
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.split(" ")[1];
        if(jwtUtil.isExpired(token)){
            System.out.println("액세스 토큰 만료됨");
            Cookie[] cookies = request.getCookies();
            boolean flag = false;
            for (Cookie cookie : cookies){
                if(cookie.getName().equals("refresh_token")){
                    String cookieToken = cookie.getValue();
                    System.out.println(cookieToken);
                    if (jwtUtil.isExpired(cookieToken)){
                        System.out.println("refresh_token 만료됨");
                        break;
                    }

                    String email = jwtUtil.getEmail(cookieToken);
                    RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(null);
                    if (refreshToken != null && refreshToken.getToken().equals(cookieToken)){
                        token = jwtUtil.createAccessToken(jwtUtil.getId(cookieToken), jwtUtil.getEmail(cookieToken), jwtUtil.getRole(cookieToken));
                        System.out.println("accessToken 재발급");
                        response.addHeader(HttpHeaders.AUTHORIZATION, token);
                        flag = true;
                    }
                    break;
                }

            }
            if (!flag){
                filterChain.doFilter(request, response);
                return;
            }
        }
        System.out.println("accesstoken 유효");
        String email =  jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        Long id = jwtUtil.getId(token);
        Member member = Member.builder()
                .id(id)
                .email(email)
                .role(role)
                .enabled(true)
                .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
