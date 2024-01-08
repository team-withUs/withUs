package com.withus.withus.notice.service;

import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;

public interface NoticeService {

  NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto);
}
