package com.withus.withus.notice.service;

import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import java.util.List;

public interface NoticeService {

  NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto, Member member);

  NoticeResponseDto updateNotice(Long clubId, Long noticeId, NoticeRequestDto requestDto, Member member);

  NoticeResponseDto getNotice(Long clubId, Long noticeId);

  void deleteNotice(Long clubId, Long noticeId, Member member);

  void createNoticeReport(Long noticeId, Member member);

  List<NoticeResponseDto> getsNotice(Long clubId, int page, int size, String sortBy);
}
