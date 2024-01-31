package com.withus.withus.domain.club.controller;

import com.withus.withus.domain.club.service.ClubMemberServiceImpl;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.notice.service.NoticeService;
import com.withus.withus.global.annotation.AuthMember;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("api/club")
public class ClubViewController {

    private final ClubMemberServiceImpl clubMemberServiceimpl;

    @GetMapping("/main-club/{clubId}")
    public String getMainClub(
        @PathVariable("clubId") Long clubId,
        Model model,
        @AuthMember Member member
    ) {
        if (member != null) {
            model.addAttribute("isLogin", true);
            model.addAttribute("memberId", member.getId());
            model.addAttribute("isClubMember", clubMemberServiceimpl.existsClubMemberByMemberIdAndClubId(member.getId(), clubId));
            model.addAttribute("isHost", clubMemberServiceimpl.isHostMember(member.getId(), clubId));

        } else {
            model.addAttribute("isLogin", false);
            model.addAttribute("isHost",false);
            model.addAttribute("isClubMember", false);

        }
        model.addAttribute("clubId", clubId);

        return "club/main-club";
    }

    @GetMapping("/{clubId}/revise-club")
    public String getReviseClub(
        Model model,
        @AuthMember Member member
    ) {
        model.addAttribute("memberId", member.getId());

        return "club/revise-club";
    }

    @GetMapping("/post-club")
    public String postClub(
        Model model,
        @AuthMember Member member
    ) {
        model.addAttribute("memberId", member.getId());

        return "club/post-club";
    }

}