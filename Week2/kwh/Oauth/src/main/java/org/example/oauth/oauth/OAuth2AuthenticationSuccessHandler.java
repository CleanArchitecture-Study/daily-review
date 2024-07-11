package org.example.oauth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.oauth.jwt.RefreshTokenRepository;
import org.example.oauth.jwt.model.RefreshToken;
import org.example.oauth.member.MemberRepository;
import org.example.oauth.member.model.Member;
import org.example.oauth.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override // 인증에 성공하면 실행할 메서드 (Authentication에 인증에 성공한 사용자 정보가 담겨있다)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> result = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickName = (String) result.get("nickname");

        // nickName으로 DB 확인
        Member member = memberRepository.findByName(nickName).orElse(null);
        if (member==null) {
            member = Member.builder()
                    .name(nickName)
                    .email("test@kakao.com")
                    .password("Kakao")
                    .enabled(true)
                    .build();
            memberRepository.save(member);
        }
        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getEmail(), member.getRole());
        System.out.println(accessToken);
        String refreshToken = jwtUtil.createRefreshToken(member.getId(), member.getEmail(), member.getRole());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .email(member.getEmail())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바 스크립트를 통해 쿠키에 접근할 수 없다.
        cookie.setMaxAge(jwtUtil.REFRESH_EXPIRE);
//        cookie.setSecure(true); // https가 아닌 통신에서는 쿠키를 전달하지 않는다.
        response.addCookie(cookie);

        getRedirectStrategy().sendRedirect(request, response, "redirectUrl");
    }
}
