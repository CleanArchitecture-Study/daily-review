package dev.us00ng.week1_login.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
