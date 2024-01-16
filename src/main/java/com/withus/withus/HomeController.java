package com.withus.withus;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.notice.dto.PageableDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {
  private final ClubService clubService;


  @GetMapping("/")
  public String home(Model model) {
    int totalListCnt = clubService.count();
    PageableDto pageableDto = new PageableDto(1, 4, "CreatedAt");
    model.addAttribute("list", clubService.getsClubByCategory(ClubCategory.ALL,pageableDto));
    model.addAttribute("count",totalListCnt);
    return "index";
  }
}

//
//  @GetMapping("/")
//  public String homeAfter() {
//    return "indexAfter";
//  }
//
//}
