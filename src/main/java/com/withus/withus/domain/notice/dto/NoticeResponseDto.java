package com.withus.withus.domain.notice.dto;



import com.withus.withus.notice.entity.Notice;
import lombok.Builder;

@Builder
public record NoticeResponseDto(
    Long id,
    String title,
    String content,
    String imageURL
) {

    public static NoticeResponseDto createNoticeResponseDto(Notice notice) {

        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .imageURL(notice.getImageURL())
                .build();
    }

}
