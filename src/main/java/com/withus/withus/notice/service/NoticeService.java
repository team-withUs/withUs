package com.withus.withus.notice.service;

import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;

public interface NoticeService {

  NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto);

  NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto);

  NoticeResponseDto getNotice(Long noticeId);

  void deleteNotice(Long noticeId);

  void updateReportNotice(Long noticeId);
}
