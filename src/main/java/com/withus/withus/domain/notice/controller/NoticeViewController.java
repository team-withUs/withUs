package com.withus.withus.domain.notice.controller;

import com.withus.withus.domain.comment.service.CommentServiceImpl;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.domain.member.entity.Member;
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
@RequestMapping("/api/club/{clubId}/notice")
public class NoticeViewController {

  private final CommentServiceImpl commentService;

  @GetMapping("/noticeAddPage")
  public String getNoticeAddPage(
      @PathVariable("clubId") Long clubId,
      Model model,
      @AuthMember Member member
  ) {
    model.addAttribute("clubId",clubId);
    model.addAttribute("memberId", member.getId());

    return "notice/noticeAdd";
  }


  @GetMapping("/{noticeId}/noticeDetailPage")
  public String getNoticeDetailPage(
      @PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId,
      Model model,
      @AuthMember Member member
  ) {
    Integer totalListCnt = commentService.count(noticeId);
    int count;

    if(totalListCnt==0){
      count=0;
    }
    else if(totalListCnt > 4){
      if(totalListCnt%4==0){
        count=totalListCnt/4;
      }
      else {
        count=totalListCnt/4+1;
      }
    }
    else {
      count=1;
    }
    List<Integer> countList = new ArrayList<>();
    for(int i=0; i<count; i++){
      countList.add(i+1);
    }

    model.addAttribute("clubId",clubId);
    model.addAttribute("noticeId",noticeId);
    model.addAttribute("countList", countList);
    model.addAttribute("memberId", member.getId());

    return "notice/noticeDetail";
  }


  @GetMapping("/{noticeId}/noticeEditPage")
  public String getNoticeEditPage(
      @PathVariable("clubId") Long clubId,
      @PathVariable("noticeId") Long noticeId,
      Model model,
      @AuthMember Member member
  ) {

    model.addAttribute("clubId", clubId);
    model.addAttribute("noticeId", noticeId);
    model.addAttribute("memberId", member.getId());

    return "notice/noticeEdit";
  }

}