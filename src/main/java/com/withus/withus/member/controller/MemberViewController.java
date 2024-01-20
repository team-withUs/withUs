package com.withus.withus.member.controller;

import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.member.entity.Member;
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

  @GetMapping("/signupPage")
  public String getSignupPage(){
    return "/auth/signupPage";
  }

  @GetMapping("/loginPage")
  public String getLoginPage(){
    return "/auth/loginPage";
  }

  @GetMapping("/profilePage/{memberId}")
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
    return "profile";
  }

  @GetMapping("/updateProfilePage/{memberId}")
  public String getUpdateProfilePage(
      @PathVariable("memberId") Long memberId,
      Model model,
      @AuthMember Member loginMember
  ) {
    model.addAttribute("memberId",loginMember.getId());
    return "updateProfile";

  }

}
