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

    Notice notice = Notice.builder().title(requestDto.title()).content(requestDto.content()).build();
    Notice saveNotice = noticeRepository.save(notice);
    return NoticeResponseDto.createNoticeResponseDto(saveNotice);
  }
}
