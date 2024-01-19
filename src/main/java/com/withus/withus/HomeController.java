package com.withus.withus;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.service.ClubServiceImpl;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final ClubServiceImpl clubService;

  @GetMapping("")
  public String home(Model model, @AuthMember Member member) {
//    Integer totalListCnt = clubService.count();
//    int count;
//    if(totalListCnt >= 4){
//      count=totalListCnt/4+1;
//    }
//    else {
//      count=1;
//    }
//    List<Integer> countList = new ArrayList<>();
//    for(int i=0; i<count; i++){
//      countList.add(i+1);
//    }
//
//    String keyWord="";
//    PageableDto pageableDto = new PageableDto(1, 4, "CreatedAt");
//    model.addAttribute("list", clubService.getsClubByCategory(ClubCategory.ALL,pageableDto, keyWord));
//    model.addAttribute("count",countList);
//    model.addAttribute("cate","ALL");
    if (member != null){
      long memberId = member.getId();
      model.addAttribute("memberId", memberId);
    }

    return "index";
  }
}
