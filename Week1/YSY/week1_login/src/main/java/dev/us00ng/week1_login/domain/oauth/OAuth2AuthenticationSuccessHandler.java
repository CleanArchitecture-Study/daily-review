package dev.us00ng.week1_login.domain.oauth;

import dev.us00ng.week1_login.config.security.jwt.TokenProvider;
import dev.us00ng.week1_login.domain.member.dto.response.TokenDto;
import dev.us00ng.week1_login.domain.member.entity.Member;
import dev.us00ng.week1_login.domain.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickName =  (String) properties.get("nickname");

        Optional<Member> memberOptional = memberRepository.findByEmail(nickName);
        if (memberOptional.isEmpty()) {
            Member member = Member.builder()
                    .email(nickName)
                    .name(nickName)
                    .createdAt(LocalDate.now())
                    .enabled(true)
                    .role("ROLE_USER")
                    .build();

            memberRepository.save(member);
        }

        TokenDto token = tokenProvider.createToken(authentication);

        Cookie accessTokenCookie = new Cookie("accessToken", token.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60 * 24);

        // Refresh token 쿠키 생성
        Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/member/login/callback");
    }
}