package com.withus.withus.notice.service;

import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import java.util.List;

public interface NoticeService {

  NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto, Member member);

  NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto, Member member);

  NoticeResponseDto getNotice(Long noticeId);

  void deleteNotice(Long noticeId, Member member);

  void updateReportNotice(Long noticeId, Member member);

  List<NoticeResponseDto> getsNotice(int page, int size, String sortBy);
}
