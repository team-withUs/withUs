package com.withus.withus.club.controller;

import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("api/club")
public class ClubViewController {
    private final NoticeService noticeService;

    @GetMapping("/main-club/{clubId}")
    public String getMainClub(
        @AuthMember Member member,
        @PathVariable("clubId") Long clubId,
        Model model
    ) {

        if (member != null) {
            model.addAttribute("isLogin", true);
            model.addAttribute("memberId", member.getId());
        } else {
            model.addAttribute("isLogin", false);
        }


        Integer totalList = noticeService.count(clubId);
        int count;
        if (totalList > 4) {
            count=totalList/3+1;
        }else{
            count=1;
        }
        List<Integer> countList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            countList.add(i + 1);
        }
        model.addAttribute("clubId", clubId);
        model.addAttribute("countList", countList);

        return "club/main-club";
    }

    @GetMapping("/{clubId}/revise-club")
    public String getReviseClub(
        @AuthMember Member member,
        Model model
    ) {
        model.addAttribute("memberId", member.getId());
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
    public String postClub(
        @AuthMember Member member,
        Model model
    ) {

        model.addAttribute("memberId", member.getId());
        return "club/post-club";
    }
    @PostMapping("/post-club")
    public String createpostClub(){
            return "club/post-club";
    }

}