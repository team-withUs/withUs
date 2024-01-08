package com.withus.withus.notice.service;


import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
  private final NoticeRepository noticeRepository;


  @Override
  public NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto) {
    Notice saveNotice = noticeRepository.save(Notice.createNotice(requestDto));
    return NoticeResponseDto.createNoticeResponseDto(saveNotice);
  }
}
