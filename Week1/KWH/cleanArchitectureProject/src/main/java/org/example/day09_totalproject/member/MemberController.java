package org.example.day09_totalproject.member;

import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.common.BaseResponse;
import org.example.day09_totalproject.emailVerify.EmailVerifyDto;
import org.example.day09_totalproject.emailVerify.EmailVerifyService;
import org.example.day09_totalproject.member.model.MemberSignupReq;
import org.example.day09_totalproject.member.model.MemberSignupRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;

    @PostMapping("/signup")
    public BaseResponse<MemberSignupRes> signup(@RequestBody MemberSignupReq memberSignupReq) {
        String uuid = memberService.sendEmail(memberSignupReq.getEmail());
        MemberSignupRes memberSignupRes = memberService.signup(memberSignupReq);
        emailVerifyService.save(EmailVerifyDto.builder()
                .email(memberSignupReq.getEmail())
                .uuid(uuid)
                .build());
        return new BaseResponse<>(memberSignupRes);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(String email, String uuid) {
        Boolean verify = emailVerifyService.verifyEmail(EmailVerifyDto.builder()
                .email(email)
                .uuid(uuid)
                .build());
        if (verify){
            memberService.activeMember(email);
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.badRequest().build();
    }

}
