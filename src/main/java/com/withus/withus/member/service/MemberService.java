package com.withus.withus.member.service;

import com.withus.withus.member.dto.MemberResponseDto;

public interface MemberService {

    MemberResponseDto getMember(Long memberId);
}
