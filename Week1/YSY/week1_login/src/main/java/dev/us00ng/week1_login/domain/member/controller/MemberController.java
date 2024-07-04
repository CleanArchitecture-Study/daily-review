package dev.us00ng.week1_login.domain.member.controller;

import dev.us00ng.week1_login.domain.member.dto.request.SignupReq;
import dev.us00ng.week1_login.domain.member.dto.response.SignupRes;
import dev.us00ng.week1_login.domain.member.service.EmailVerifyService;
import dev.us00ng.week1_login.domain.member.service.MemberService;
import dev.us00ng.week1_login.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<BaseResponse<SignupRes>> signup(@Valid @RequestBody SignupReq req) {
        String uuid = memberService.sendEmail(req.getEmail());
        emailVerifyService.save(req.getEmail(), uuid);

        return ResponseEntity.ok(new BaseResponse<>(memberService.signup(req)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/verify")
    public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String uuid) {
        HttpHeaders header = new HttpHeaders();

        if(emailVerifyService.isExist(email, uuid)) {
            memberService.activeMember(email);
            header.add(HttpHeaders.LOCATION,"http://localhost:3000");
        } else {
            header.add(HttpHeaders.LOCATION, "http://localhost:3000/member/signup");
        }
        return new ResponseEntity<>(header, HttpStatus.MOVED_PERMANENTLY);
    }


}
