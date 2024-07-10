package org.example.oauth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.oauth.member.MemberRepository;
import org.example.oauth.member.model.Member;
import org.example.oauth.util.JwtUtil;
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
        String token = jwtUtil.createToken(member.getId(), member.getEmail(), member.getRole());

        Cookie aToken = new Cookie("token", token);
        aToken.setPath("/");
        aToken.setMaxAge(60*60*5);
        response.addCookie(aToken);

        getRedirectStrategy().sendRedirect(request, response, "redirectUrl");
    }
}
