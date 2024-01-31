package com.withus.withus.domain.club.entity;
public enum ClubCategory {
    ALL("전체"),
    SPORTS("운동"),
    TODAY("일상"),
    GAME("게임"),
    FOOD("음식"),
    STUDY("스터디"),
    ETC("기타");

    private final String KrName;

    ClubCategory(String KrName) {
        this.KrName = KrName;
    }
    public String getKrName() {
        return KrName;
    }
}

