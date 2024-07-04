package dev.us00ng.week1_login.domain.member.service;

import static dev.us00ng.week1_login.global.common.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;
import static dev.us00ng.week1_login.global.common.BaseResponseStatus.USER_NOT_FOUND;

import dev.us00ng.week1_login.domain.member.dto.request.SignupReq;
import dev.us00ng.week1_login.domain.member.dto.response.SignupRes;
import dev.us00ng.week1_login.domain.member.entity.Member;
import dev.us00ng.week1_login.domain.member.mapper.MemberMapper;
import dev.us00ng.week1_login.domain.member.repository.MemberRepository;
import dev.us00ng.week1_login.global.common.BaseException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JavaMailSender emailSender;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignupRes signup(SignupReq dto) {
        validateDuplicateEmail(dto.getEmail());
        Member member = memberMapper.toEntity(dto, bCryptPasswordEncoder);
        memberRepository.save(member);

        return memberMapper.toDto(member);
    }

    private void validateDuplicateEmail(final String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
    }


    public Member getMemberByEmail(String email) {
       return memberRepository.findByEmail(email).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
    }

    public String sendEmail(String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(username);
        message.setSubject("[내 사이트] 가입 환영");
        String uuid = UUID.randomUUID().toString();
        message.setText("http://localhost:8080/member/verify?email="+username+"&uuid="+uuid);

        emailSender.send(message);

        return uuid;
    }

    public void activeMember(String email) {
        Member target = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        target.setEnabled(true);

        memberRepository.save(target);
    }
}
