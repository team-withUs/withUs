package com.withus.withus.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record ReportRequestDto(
    @NotBlank(message = "신고 내용을 작성해주세요.")
    String content
) {

}
