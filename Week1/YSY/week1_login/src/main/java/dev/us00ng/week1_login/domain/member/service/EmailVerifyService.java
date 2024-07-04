package dev.us00ng.week1_login.domain.member.service;

import dev.us00ng.week1_login.domain.member.entity.EmailVerify;
import dev.us00ng.week1_login.domain.member.repository.EmailVerifyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailVerifyRepository emailVerifyRepository;

    public Boolean isExist(String email, String uuid) {
        Optional<EmailVerify> result = emailVerifyRepository.findByEmail(email);
        if(result.isPresent()) {
            EmailVerify emailVerify = result.get();
            if(emailVerify.getUuid().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    public void save(String email, String uuid) {
        EmailVerify emailVerify = EmailVerify.builder()
                .email(email)
                .uuid(uuid)
                .build();

        emailVerifyRepository.save(emailVerify);
    }
}
