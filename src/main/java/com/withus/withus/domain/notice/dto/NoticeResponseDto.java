package com.withus.withus.domain.notice.dto;

import com.withus.withus.domain.notice.entity.Notice;

import lombok.Builder;


@Builder
public record NoticeResponseDto(
        Long id,
        String title,
        String content,
        String imageURL

) {

    public static NoticeResponseDto createNoticeResponseDto(Notice notice) {
        Long id = notice.getId();
        String title = notice.getTitle();
        String content = notice.getContent();
        String imageURL = notice.getImageURL();

        return NoticeResponseDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .imageURL(imageURL)
                .build();
    }
}
