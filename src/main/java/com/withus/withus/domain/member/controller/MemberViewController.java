package com.withus.withus.domain.member.controller;

import com.withus.withus.domain.member.service.MemberServiceImpl;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberViewController {

  private final MemberServiceImpl memberService;

  @GetMapping("/signupPage")
  public String getSignupPage(){
    return "auth/signupPage";
  }

  @GetMapping("/loginPage")
  public String getLoginPage(){
    return "auth/loginPage";
  }

  @GetMapping("/{memberId}/profilePage")
  public String getProfilePage(
      @PathVariable("memberId") Long memberId,
      Model model,
      @AuthMember Member loginMember
  ){
    if (loginMember.getId().equals(memberId)) {
      model.addAttribute("isSameMember", true);
    } else {
      model.addAttribute("isSameMember", false);
    }
    model.addAttribute("memberId", memberId);
    model.addAttribute("loginMemberId", loginMember.getId());
    return "profile";
  }

  @GetMapping("/{memberId}/updateProfilePage")
  public String getUpdateProfilePage(
      @PathVariable("memberId") Long memberId,
      Model model,
      @AuthMember Member loginMember
  ) {
    if (loginMember.getId().equals(memberId)) {
      model.addAttribute("isSameMember", true);
    } else {
      model.addAttribute("isSameMember", false);
    }
    model.addAttribute("memberId",memberId);
    model.addAttribute("loginMemberId",loginMember.getId());
    return "updateProfile";

  }

  @GetMapping("/{memberId}/myHostingClubPage")
  public String myHostingClub(
      @PathVariable("memberId") Long memberId,
      @AuthMember Member member,
      Model model
  ) {

    model.addAttribute("memberId",memberId);
    model.addAttribute("loginMemberId", member.getId());

    return "myClub";
  }

}
