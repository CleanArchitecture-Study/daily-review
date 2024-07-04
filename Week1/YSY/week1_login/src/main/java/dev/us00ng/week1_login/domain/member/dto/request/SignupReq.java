package dev.us00ng.week1_login.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupReq {
    @Email
    @NotNull
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
