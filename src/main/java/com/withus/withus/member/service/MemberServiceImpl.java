package com.withus.withus.member.service;

import static com.withus.withus.member.dto.MemberResponseDto.buildMemberResponseDto;

import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDto getMember(Long memberId) {
        Member member = findMemberByMemberId(memberId);

        return buildMemberResponseDto(member);
    }

    public Member findMemberByMemberId(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(
            ()-> new BisException(ErrorCode.NOT_FOUND_MEMBER)
        );
    }
}
