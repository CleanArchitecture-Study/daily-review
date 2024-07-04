package dev.us00ng.week1_login.domain.member.dto.response;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SignupRes {
    private Long id;
    private String email;
    private String name;
}
