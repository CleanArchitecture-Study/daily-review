package org.example.day09_totalproject.emailVerify;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerifyDto {
    private String email;
    private String uuid;

    public EmailVerify toEmailVerify() {
        return EmailVerify.builder()
                .email(email)
                .uuid(uuid)
                .build();
    }
}
