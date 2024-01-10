package com.withus.withus.member.controller;

import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.dto.ReportRequestDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.dto.UpdateRequestDto;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

  private final MemberServiceImpl memberService;


  @PostMapping("/signup/email")
  public ResponseEntity<CommonResponse<EmailRequestDto>> authenticationEmail (
      @Valid @RequestBody EmailRequestDto emailRequestDto
  ) {
    memberService.sendAuthCodeToEmail(emailRequestDto);
    return ResponseEntity.status(ResponseCode.SEND_MAIL.getHttpStatus()).body(CommonResponse.of(ResponseCode.SEND_MAIL, emailRequestDto));
  }

  @PostMapping("/signup")
  public ResponseEntity<CommonResponse<String>> signupMember (
      @Valid @RequestBody SignupRequestDto signupRequestDto
  ) {

    memberService.signup(signupRequestDto);
    return ResponseEntity.status(ResponseCode.SIGNUP.getHttpStatus()).body(CommonResponse.of(ResponseCode.SIGNUP, ""));
  }

  @GetMapping("/{memberId}")
  public ResponseEntity<CommonResponse<MemberResponseDto>> getMember(
      @PathVariable("memberId") Long memberId
  ) {
    MemberResponseDto memberResponseDto = memberService.getMember(memberId);
    return ResponseEntity
        .status(ResponseCode.GET_PROFILE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.GET_PROFILE, memberResponseDto));
  }

  @PatchMapping("/{memberId}")
  public ResponseEntity<CommonResponse<MemberResponseDto>> updateMember(
      @PathVariable("memberId") Long memberId,
      @Valid @RequestBody UpdateRequestDto updateRequestDto,
      @AuthMember Member member
  ) {
    MemberResponseDto memberResponseDto = memberService.updateMember(
        memberId,
        updateRequestDto,
        member
    );

    return ResponseEntity.status(ResponseCode.UPDATE_PROFILE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.UPDATE_PROFILE, memberResponseDto));
  }

  @DeleteMapping("/{memberId}")
  public ResponseEntity<CommonResponse<String>> deleteMember(
      @PathVariable("memberId") Long memberId,
      @AuthMember Member member
  ) {
      memberService.deleteMember(memberId, member);

      return ResponseEntity
          .status(ResponseCode.RESIGN_MEMBER.getHttpStatus())
          .body(CommonResponse.of(ResponseCode.RESIGN_MEMBER,""));
  }

  @PostMapping("/{memberId}/report")
  public ResponseEntity<CommonResponse<String>> reportMember(
      @PathVariable("memberId") Long memberId,
      @RequestBody ReportRequestDto reportRequestDto,
      @AuthMember Member member
  ) {
    memberService.reportMember(memberId,reportRequestDto,member);
    return ResponseEntity
        .status(ResponseCode.INVITE_MEMBER.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_MEMBER_REPORT,""));
  }

  @GetMapping("/club")
  public ResponseEntity<CommonResponse<List<ClubResponseDto>>> getMyClubList(
      Pageable pageable,
      @AuthMember Member member
  ) {
    List<ClubResponseDto> clubResponseDtoList = memberService.getMyClubList(pageable,member);

    return ResponseEntity
        .status(ResponseCode.GET_MY_CLUBLIST.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.GET_MY_CLUBLIST,clubResponseDtoList));
  }

  @PostMapping("/{memberId}/club/{clubId}")
  public ResponseEntity<CommonResponse<String>> inviteMember(
      @PathVariable("memberId") Long memberId,
      @PathVariable("clubId") Long clubId,
      @AuthMember Member member
  ) {
    memberService.inviteMember(memberId,clubId,member);

    return ResponseEntity.status(ResponseCode.INVITE_MEMBER.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.INVITE_MEMBER,""));
  }
}
