package com.withus.withus.notice.service;


import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
  private final NoticeRepository noticeRepository;


  @Override
  public NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto) {
    Notice saveNotice = noticeRepository.save(Notice.createNotice(requestDto));
    return NoticeResponseDto.createNoticeResponseDto(saveNotice);
  }

  @Transactional
  @Override
  public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
    Notice notice = findByIsActiveAndNoticeId(noticeId);
    notice.update(requestDto);
    return NoticeResponseDto.createNoticeResponseDto(notice);
  }

  @Override
  public NoticeResponseDto getNotice(Long noticeId) {
    Notice notice = findByIsActiveAndNoticeId(noticeId);
    return NoticeResponseDto.createNoticeResponseDto(notice);
  }

  @Transactional
  @Override
  public void deleteNotice(Long noticeId) {
    Notice notice = findByIsActiveAndNoticeId(noticeId);
    notice.delete();
  }

  @Transactional
  @Override
  public void updateReportNotice(Long noticeId) {
    Notice notice = findByIsActiveAndNoticeId(noticeId);
    notice.updateReport(notice.getReport()+1);
    if(notice.getReport()>=3){
      notice.delete();
    }
  }

  public Notice findByIsActiveAndNoticeId(Long noticeId) {
    Notice notice = noticeRepository.findByIsActiveAndId(true,noticeId)
        .orElseThrow(()->new BisException(ErrorCode.NOT_FOUND_NOTICE));
    return notice;
  }
}
