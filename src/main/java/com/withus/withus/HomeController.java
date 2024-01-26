package com.withus.withus;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.service.ClubServiceImpl;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final ClubServiceImpl clubService;

  @GetMapping("")
  public String home(Model model, @AuthMember Member member) {

    Integer totalListCnt = clubService.count();
    double count;
    if(totalListCnt > 4){
      count= (double) totalListCnt /4 ;
    }
    else {
      count=1;
    }
    List<Integer> countList = new ArrayList<>();
    for(int i=0; i<count; i++){
      countList.add(i+1);
    }
    if (member != null){
      long memberId = member.getId();
      model.addAttribute("memberId", memberId);
    }
    model.addAttribute("countList", countList);

    return "index";
  }

}
