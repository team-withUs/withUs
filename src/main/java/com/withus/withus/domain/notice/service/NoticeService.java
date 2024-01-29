package com.withus.withus.domain.notice.service;

import com.withus.withus.domain.notice.dto.NoticeRequestDto;
import com.withus.withus.domain.notice.dto.NoticeResponseDto;
import com.withus.withus.domain.notice.dto.PageableDto;
import com.withus.withus.domain.notice.dto.ReportRequestDto;
import com.withus.withus.domain.member.entity.Member;
import java.util.List;

public interface NoticeService {

  NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto, Member member);

  NoticeResponseDto updateNotice(Long clubId, Long noticeId, NoticeRequestDto requestDto, Member member);

  NoticeResponseDto getNotice(Long clubId, Long noticeId, Member member);

  void deleteNotice(Long clubId, Long noticeId, Member member);

  void createReportNotice(Long noticeId, ReportRequestDto requestDto, Member member);

  List<NoticeResponseDto> getsNotice(Long clubId, PageableDto pageableDto);

  Integer count(Long clubId);
}
