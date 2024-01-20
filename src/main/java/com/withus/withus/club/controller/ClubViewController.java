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

//    @GetMapping("/{clubId}/notice")
//    public String getNotice(
//            @PathVariable("clubId") Long clubId,
//            Model model
//    ) {
//        List<NoticeResponseDto> noticeList = noticeService.getsNotice(clubId, PageableDto.getsPageableDto(1 , 2, "createdAt"));
//
//        int count;
//        if (noticeList.size() > 3) {
//            count = (noticeList.size() / 4) + 1;
//        } else {
//            count = 1;
//        }
//        List<Integer> countList = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            countList.add(i + 1);
//        }
//        model.addAttribute("clubId", clubId);
//        model.addAttribute("noticeList", noticeList);
//        model.addAttribute("countList", countList);
//
//        return "club/club-main/{clubId}/clubnotice";
//    }

}