package com.withus.withus.chat.controller;

import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatViewController {

  private final MemberServiceImpl memberService;

  @GetMapping("/chatRoom/{roomId}")
  public String chatRoomPage(
      @PathVariable("roomId") Long roomId,
      Model model,
      @AuthMember Member member
  ) {
    member = memberService.findMemberByLoginname("junwoo");

    model.addAttribute("roomId", roomId);
    model.addAttribute("memberName", member.getUsername());
    model.addAttribute("memberId", member.getId());
      return "/chat/chatRoom";
  }

  @GetMapping("/chatRoomList/{memberId}")
  public String chatRoomListPage(
      @PathVariable("memberId") Long memberId,
      Model model
  ) {
    model.addAttribute("memberId", memberId);
    return "/chat/chatRoomList";
  }
}
