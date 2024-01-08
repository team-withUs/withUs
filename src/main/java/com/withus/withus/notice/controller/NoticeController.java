package com.withus.withus.notice.controller;


import com.withus.withus.global.response.ResponseCode;

import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
  private final NoticeService noticeService;


  @PostMapping("/{clubId}")
  public ResponseEntity<CommonResponse<NoticeResponseDto>> createNotice(
      @PathVariable("clubId") Long clubId,
      @RequestBody NoticeRequestDto requestDto
  ) {
    NoticeResponseDto responseDto = noticeService.createNotice(clubId, requestDto);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_CREATE.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_CREATE,responseDto));
  }

  @PatchMapping("/{noticeId}")
  public ResponseEntity<CommonResponse<NoticeResponseDto>> updateNotice(
      @PathVariable("noticeId") Long noticeId,
      @RequestBody NoticeRequestDto requestDto
  ) {
    NoticeResponseDto responseDto = noticeService.updateNotice(noticeId, requestDto);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_UPDATE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_UPDATE,responseDto));
  }

  @GetMapping("/{noticeId}")
  public ResponseEntity<CommonResponse<NoticeResponseDto>> getNotice(
      @PathVariable("noticeId") Long noticeId
  ) {
    NoticeResponseDto responseDto = noticeService.getNotice(noticeId);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_GET.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_GET,responseDto));
  }

  @DeleteMapping("/{noticeId}")
  public ResponseEntity<CommonResponse> deleteNotice(
      @PathVariable("noticeId") Long noticeId
  ) {
    noticeService.deleteNotice(noticeId);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_DELETE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_DELETE,""));
  }

}
