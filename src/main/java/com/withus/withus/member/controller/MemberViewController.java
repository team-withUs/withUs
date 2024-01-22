package com.withus.withus.member.controller;

import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    model.addAttribute("loginMemberId", loginMember.getId());
    return "profile";
  }

  @GetMapping("/updateProfilePage/{memberId}")
  public String getUpdateProfilePage(
      @PathVariable("memberId") Long memberId,
      Model model,
      @AuthMember Member loginMember
  ) {

    model.addAttribute("memberId",memberId);
    model.addAttribute("loginMemberId",loginMember.getId());
    return "updateProfile";

  }

  @GetMapping("/myClub/{memberId}")
  public String myHostingClub(
      @PathVariable("memberId") Long memberId,
      Model model
  ) {

    model.addAttribute("memberId",memberId);

    return "myClub";
  }

}
