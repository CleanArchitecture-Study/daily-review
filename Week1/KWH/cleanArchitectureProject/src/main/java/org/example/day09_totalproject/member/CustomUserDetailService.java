package org.example.day09_totalproject.member;

import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.member.model.CustomUserDetails;
import org.example.day09_totalproject.member.model.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // 5번
    // Provider가 UserDetailService 실행
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        if (!member.getEnabled()){
            throw new UsernameNotFoundException(email);
        }
        // 6번 (객체 생성), 7번 (반환)
        return new CustomUserDetails(member);
    }
}
