package com.withus.withus.member.service;

import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.dto.ReportRequestDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.dto.UpdateRequestDto;
import com.withus.withus.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

  void sendAuthCodeToEmail(EmailRequestDto emailRequestDto);

  void signup(SignupRequestDto signupRequestDto);

  MemberResponseDto getMember(Long memberId);

  MemberResponseDto updateMember(Long memberId, UpdateRequestDto updateRequestDto, Member member);

  void deleteMember(Long memberId, Member member);

  void reportMember(Long memberId, ReportRequestDto reportRequestDto, Member member);

  Page<ClubResponseDto> getMyClubList(Pageable pageable, Member member);

  void inviteMember(Long memberId, Long clubId, Member member);

}
