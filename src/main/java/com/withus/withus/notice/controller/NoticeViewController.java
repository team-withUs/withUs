package com.withus.withus.notice.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/club/{clubId}/notice")
public class NoticeViewController {


  @GetMapping("/noticeAddPage")
  public String getNoticeAddPage(@PathVariable("clubId") Long clubId, Model model){
    model.addAttribute("clubId",clubId);
    return "notice/noticeAdd";
  }


  @GetMapping("/{noticeId}/noticeDetailPage")
  public String getNoticeDetailPage(@PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId, Model model){
    model.addAttribute("clubId",clubId);
    model.addAttribute("noticeId",noticeId);
    return "notice/noticeDetail";
  }


  @GetMapping("/{noticeId}/noticeEditPage")
  public String getNoticeEditPage(@PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId, Model model){
    model.addAttribute("clubId",clubId);
    model.addAttribute("noticeId",noticeId);
    return "notice/noticeEdit";
  }


}
