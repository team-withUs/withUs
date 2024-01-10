package com.withus.withus.notice.controller;


import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.ResponseCode;

import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.dto.ReportRequestDto;
import com.withus.withus.notice.service.NoticeService;
import java.util.List;
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
@RequestMapping("/api")
public class NoticeController {
  private final NoticeService noticeService;


  @PostMapping("/club/{clubId}/notice")
  public ResponseEntity<CommonResponse<NoticeResponseDto>> createNotice(
      @PathVariable("clubId") Long clubId,
      @RequestBody NoticeRequestDto requestDto,
      @AuthMember Member member
  ) {
    NoticeResponseDto responseDto = noticeService.createNotice(clubId, requestDto, member);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_CREATE.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_CREATE,responseDto));
  }

  @PatchMapping("/club/{clubId}/notice/{noticeId}")
  public ResponseEntity<CommonResponse<NoticeResponseDto>> updateNotice(
      @PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId,
      @RequestBody NoticeRequestDto requestDto,
      @AuthMember Member member
  ) {
    NoticeResponseDto responseDto = noticeService.updateNotice(clubId, noticeId, requestDto, member);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_UPDATE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_UPDATE, responseDto));
  }

  @GetMapping("/club/{clubId}/notice/{noticeId}")
  public ResponseEntity<CommonResponse<NoticeResponseDto>> getNotice(
      @PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId
  ) {
    NoticeResponseDto responseDto = noticeService.getNotice(clubId, noticeId);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_GET.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_GET,responseDto));
  }

  @GetMapping("/club/{clubId}/notice")
  public ResponseEntity<CommonResponse<List<NoticeResponseDto>>> getsNotice(
      @PathVariable("clubId") Long clubId,
      PageableDto pageableDto

  ) {
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_GETS.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_GETS,
            noticeService.getsNotice(clubId, pageableDto)));
  }

  @DeleteMapping("/club/{clubId}/notice/{noticeId}")
  public ResponseEntity<CommonResponse<String>> deleteNotice(
      @PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId,
      @AuthMember Member member
  ) {
    noticeService.deleteNotice(clubId, noticeId, member);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_DELETE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_DELETE,""));
  }

  @PostMapping("notice/{noticeId}/report")
  public ResponseEntity<CommonResponse<String>> createReportNotice(
      @PathVariable("noticeId") Long noticeId,
      @RequestBody ReportRequestDto requestDto,
      @AuthMember Member member
  ) {
    noticeService.createReportNotice(noticeId, requestDto, member);
    return ResponseEntity.status(ResponseCode.SUCCESS_NOTICE_REPORT.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_NOTICE_REPORT,""));
  }

}
