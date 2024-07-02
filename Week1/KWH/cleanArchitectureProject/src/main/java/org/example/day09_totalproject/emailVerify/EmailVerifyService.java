package org.example.day09_totalproject.emailVerify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailVerifyRepository emailVerifyRepository;

    public void save(EmailVerifyDto emailVerifyDto){
        EmailVerify emailVerify = emailVerifyDto.toEmailVerify();
        emailVerifyRepository.save(emailVerify);
    }

    public Boolean verifyEmail(EmailVerifyDto emailVerifyDto){
        Optional<EmailVerify> emailVerifyOptional = emailVerifyRepository.findByEmail(emailVerifyDto.getEmail());
        if(emailVerifyOptional.isPresent()){
            EmailVerify emailVerify = emailVerifyOptional.get();
            return emailVerify.getUuid().equals(emailVerifyDto.getUuid());
        }
        return false;
    }

}
