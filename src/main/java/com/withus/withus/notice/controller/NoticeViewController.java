package com.withus.withus.notice.controller;


import com.withus.withus.comment.service.CommentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/club/{clubId}/notice")
public class NoticeViewController {
  private final CommentServiceImpl commentService;


  @GetMapping("/noticeAddPage")
  public String getNoticeAddPage(@PathVariable("clubId") Long clubId, Model model){
    model.addAttribute("clubId",clubId);
    return "notice/noticeAdd";
  }


  @GetMapping("/{noticeId}/noticeDetailPage")
  public String getNoticeDetailPage(@PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId, Model model){
    Integer totalListCnt = commentService.count(noticeId);
    model.addAttribute("clubId",clubId);
    model.addAttribute("noticeId",noticeId);
    model.addAttribute("totalListCnt", totalListCnt);
    return "notice/noticeDetail";
  }


  @GetMapping("/{noticeId}/noticeEditPage")
  public String getNoticeEditPage(@PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId, Model model){

    model.addAttribute("clubId", clubId);
    model.addAttribute("noticeId", noticeId);
    return "notice/noticeEdit";
  }


}
