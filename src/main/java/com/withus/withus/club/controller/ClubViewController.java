package com.withus.withus.club.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/club")
public class ClubViewController {

    @GetMapping("/main-club/{clubId}")
    public String getMainClub() {
        return "club/main-club";
    }

    @GetMapping("/{clubId}/revise-club")
    public String getReviseClub() {
        return "club/revise-club";
    }

    @PostMapping("/{clubId}/revise-club")
    public String postReviseClub() {
        return "club/revise-club";
    }

    @PatchMapping("/{clubId}/revise-club")
    public String patchReviseClub(){
        return "club/revise-club";
    }

    @GetMapping("/post-club")
    public String postClub() {
        return "club/post-club";
    }
    @PostMapping("/post-club")
    public String createpostClub(){
            return "club/post-club";
    }
}