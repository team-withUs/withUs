package com.withus.withus.domain.notice.dto;

import org.springframework.web.multipart.MultipartFile;

public record NoticeRequestDto(
  String title,
  String content,
  String category,
  MultipartFile imageFile
) {

}
