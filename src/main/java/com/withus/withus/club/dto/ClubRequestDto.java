package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubRequestDto {
    private String clubTitle;
    private String content;
    private String category;
    private String image;
    private int maxMember;
    private String startTime;
    private String endTime;

    public ClubCategory getCategoryAsEnum() {
        return ClubCategory.valueOf(this.category.toUpperCase());
    }
}