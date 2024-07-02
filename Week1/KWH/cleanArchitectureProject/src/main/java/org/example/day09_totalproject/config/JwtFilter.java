package org.example.day09_totalproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.jwt.JwtUtil;
import org.example.day09_totalproject.member.model.CustomUserDetails;
import org.example.day09_totalproject.member.model.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 토큰을 확인하는 필터
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { //사용자가 요청을 보낼 때 한번 실행하는 필터
    private final JwtUtil jwtUtil;

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
            System.out.println("토큰 만료됨");
            filterChain.doFilter(request, response);
            return;
        }

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
        // 인증된 토큰을 등록한다
        SecurityContextHolder.getContext().setAuthentication(authToken); // 10번
        filterChain.doFilter(request, response);
    }
}
