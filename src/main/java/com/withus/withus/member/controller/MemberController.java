package com.withus.withus.member.controller;

import com.withus.withus.global.Response.CommonResponse;
import com.withus.withus.global.Response.ResponseCode;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<MemberResponseDto>> getMember(
        @PathVariable Long memberId
    ){
        MemberResponseDto memberResponseDto = memberService.getMember(memberId);
        return ResponseEntity.status(ResponseCode.GET_PROFILE.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.GET_PROFILE, memberResponseDto));
    }
}
