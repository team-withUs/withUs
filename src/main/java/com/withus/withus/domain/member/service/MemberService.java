package com.withus.withus.domain.member.service;

import com.withus.withus.domain.club.dto.ClubResponseDto;
import com.withus.withus.domain.member.dto.PasswordRequestDto;
import com.withus.withus.domain.member.dto.ReportRequestDto;
import com.withus.withus.domain.member.dto.SignupRequestDto;
import com.withus.withus.domain.member.dto.UpdateRequestDto;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.member.dto.EmailRequestDto;
import com.withus.withus.domain.member.dto.MemberResponseDto;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface MemberService {

    void sendAuthCodeToEmail(EmailRequestDto emailRequestDto);

    void signup(SignupRequestDto signupRequestDto);

    MemberResponseDto getMember(Long memberId);

    MemberResponseDto updateMember(Long memberId, UpdateRequestDto updateRequestDto, Member member);

    MemberResponseDto updatePassword(Long memberId, PasswordRequestDto passwordRequestDto, Member member);

    void deleteMember(Long memberId, Member member);

    void reportMember(Long memberId, ReportRequestDto reportRequestDto, Member member);

//    List<ClubResponseDto> getMyClubList(Pageable pageable, Member member);


    void inviteMember(Long memberId, Long clubId, Member member);

    Page<ClubResponseDto> getMyClubList(Pageable pageable, Member member);

    Page<ClubResponseDto> getMyHostingClubList(Pageable pageable, Long memberId);
    MemberResponseDto getMemberEmail(String email);

}
