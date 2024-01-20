package com.withus.withus.club.controller;

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
            @PathVariable("clubId") Long clubId,
            Model model
    ) {
        Integer totalList = noticeService.count(clubId);
        int count;
        if (totalList > 3) {
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

//    @GetMapping("/main-club/{clubId}")
//    public String getNotice(
//            @PathVariable("clubId") Long clubId,
//            Model model
//    ) {
//        Integer totalList = noticeService.count(clubId);
//        int count;
//        if (totalList > 4) {
//            count=totalList/4+1;
//        }else{
//            count=1;
//        }
//        List<Integer> countList = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            countList.add(i + 1);
//        }
//        System.out.println("===================================="+countList.size());
//        model.addAttribute("clubId", clubId);
//        model.addAttribute("countList", countList);

//        return "club/main-club";
//    }

}