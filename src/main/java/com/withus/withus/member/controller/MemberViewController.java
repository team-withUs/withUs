package com.withus.withus.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/member")
public class MemberViewController {

  @GetMapping("/signupPage")
  public String getSignupPage(){
    return "signupPage";
  }

  @GetMapping("/loginPage")
  public String getLoginPage(){
    return "loginPage";
  }

  @GetMapping("/profilePage")
  public String getProfilePage(){
    return "profile";
  }

}
