package org.example.day09_totalproject.member;

import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.member.model.MemberSignupReq;
import org.example.day09_totalproject.member.model.MemberSignupRes;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberSignupRes signup(MemberSignupReq memberSignupReq) {
        //암호화
        // $2a $10 $JZlykI0aZ9THG80rfYIwb.X0rHDN83Hgd7AEDu4DqQBgWs/Y4JBzS   암호문
        // $암호화방식 $SALT $암호화된 비밀번호
        // qwer1234 -> 랜덤한 SALT값 생성 -> 암호화(SALT값, 비밀번호) -> 암호문
        // 스프링 시큐리티
        //      DB에서 암호문을 조회 -> 암호문에서 SALT를 가져온다 -> 암호화(가져온 SALT, 사용자 비밀번호) -> 새 암호문
        String nowTime = String.valueOf(LocalDateTime.now());
        Member member = Member.builder()
                .email(memberSignupReq.getEmail())
                .password(bCryptPasswordEncoder.encode(memberSignupReq.getPassword()))
                .name(memberSignupReq.getName())
                .role("ROLE_USER")
                .enabled(false)
                .createdAt(nowTime)
                .updatedAt(nowTime)
                .build();
        memberRepository.save(member);

        return member.toMemberSignupRes();
    }

    public String sendEmail(String email){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email); // 받는 사람 메일
        simpleMailMessage.setSubject("[내 사이트] 가입 환영"); // 메일 제목
        String uuid = UUID.randomUUID().toString();
        simpleMailMessage.setText("http://localhost:8080/member/verify?email="+email+"&uuid="+uuid); // 메일 내용

        emailSender.send(simpleMailMessage); // 메일 보내기
        return uuid;
    }

    public void activeMember(String email){
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isPresent()){
            Member member = memberOptional.get();
            member.setEnabled();
            memberRepository.save(member);
        }
    }

}
