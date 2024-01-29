package com.withus.withus.global.scheduler;


import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.repository.ClubMemberRepository;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.comment.entity.Comment;
import com.withus.withus.comment.repository.CommentRepository;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.repository.NoticeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "삭제")
@Component
@RequiredArgsConstructor
public class DeleteScheduler {
  private final MemberRepository memberRepository;
  private final ClubRepository clubRepository;
  private final ClubMemberRepository clubMemberRepository;
  private final NoticeRepository noticeRepository;
  private final CommentRepository commentRepository;

  LocalDateTime realNow = LocalDateTime.now();


  //매일 23시에 한번 1주일 지난거(isActive가 false 인것만) 삭제
  @Scheduled(cron = "0 0 20 * * * ")
  public void deleteAutoOneWeek(){
    log.info(" ========== 삭제 스케줄러 ========== ");
    deleteClub();
    deleteNotice();
    deleteComment();

  }
  public void deleteMember(){
    log.info(" ========== 멤버 삭제 ========== ");
    List<Member> memberList = memberRepository.findAllByIsActive(false);
    if(!memberList.isEmpty()){   //리스트 확인
      for(int i=0; i < memberList.size(); i++){
        LocalDateTime oneWeek = memberList.get(i).getModifiedAt().plusDays(7);
        if(!realNow.isAfter(oneWeek)){
          memberList.remove(memberList.get(i));
        }
      }

      if(!memberList.isEmpty()){   // 리스트 확인
        for(Member member : memberList){
          List<Club>  clubList = clubRepository.findAllByMemberId(member.getId());    //멤버가 작성한 클럽
          List<ClubMember> clubMemberList = clubMemberRepository.findAllByMemberId(member.getId());  //멤버가 들어있는 클럽멤버
          if(!clubList.isEmpty()){
            for(Club club : clubList){
              List<Notice> noticeList = noticeRepository.findAllByClubId(club.getId());     //멤버가 작성한 클럽에 있는 노티스
              if(!noticeList.isEmpty()){
                for(Notice notice : noticeList){
                  List<Comment> commentList = commentRepository.findAllByNoticeId(notice.getId()); //멤버가 작성한 클럽에 있는 노티스안에 있는 댓글
                  if(!commentList.isEmpty()){
                    commentRepository.deleteAllInBatch(commentList);
                  }
                }
                noticeRepository.deleteAllInBatch(noticeList);
              }
            }


            List<Notice> noticeList = noticeRepository.findAllByMemberId(member.getId());  //멤버가 작성한것
            if(!noticeList.isEmpty()){
              for(Notice notice : noticeList){
                List<Comment> commentList = commentRepository.findAllByNoticeId(notice.getId());   //멤버가 작성한 노티스의 댓글
                if(!commentList.isEmpty()){
                  commentRepository.deleteAllInBatch(commentList);
                }
              }
              noticeRepository.deleteAllInBatch(noticeList);
            }


            List<Comment> commentList = commentRepository.findAllByMemberId(member.getId());   //멤버가 작성한 댓글
            if(!commentList.isEmpty()){
              commentRepository.deleteAllInBatch(commentList);
            }




            clubRepository.deleteAllInBatch(clubList);
          }
          if(!clubMemberList.isEmpty()){
            clubMemberRepository.deleteAllInBatch(clubMemberList);
          }
        }
      }





    }
  }



  public void deleteClub(){
    List<Club> clubList = clubRepository.findAllByIsActive(false);
    if(!clubList.isEmpty()){
      for(int i=0; i < clubList.size(); i++){
        LocalDateTime oneWeek = clubList.get(i).getModifiedAt().plusDays(7);
        if(!realNow.isAfter(oneWeek)){
          clubList.remove(clubList.get(i));
        }
      }
      if(!clubList.isEmpty()){
        for(Club club : clubList){
          List<ClubMember> clubMemberList = clubMemberRepository.findAllByClubId(club.getId());
          if(!clubMemberList.isEmpty()){
            clubMemberRepository.deleteAllInBatch(clubMemberList);
          }
          List<Notice> noticeList = noticeRepository.findAllByClubId(club.getId());
          if(!noticeList.isEmpty()){
            for(Notice notice : noticeList){
              List<Comment> commentList = commentRepository.findAllByNoticeId(notice.getId());
              if(!commentList.isEmpty()){
                commentRepository.deleteAllInBatch(commentList);
              }
            }
            noticeRepository.deleteAllInBatch(noticeList);
          }
        }
        clubRepository.deleteAllInBatch(clubList);
      }
    }
  }




  public void deleteNotice(){
    List<Notice> noticeList = noticeRepository.findAllByIsActive(false);
    if(!noticeList.isEmpty()){
      for(int i=0; i < noticeList.size(); i++){
        LocalDateTime oneWeek = noticeList.get(i).getModifiedAt().plusDays(7);
        if(!realNow.isAfter(oneWeek)){
          noticeList.remove(noticeList.get(i));
        }
      }

      if(!noticeList.isEmpty()){
        for(Notice notice : noticeList){
          List<Comment> commentList = commentRepository.findAllByNoticeId(notice.getId());
          if(!commentList.isEmpty()){
           commentRepository.deleteAllInBatch(commentList);
          }
        }
        noticeRepository.deleteAllInBatch(noticeList);
      }
    }

  }

  public void deleteComment(){
    List<Comment> commentList = commentRepository.findAllByIsActive(false);
    if(!commentList.isEmpty()){
      for(int i=0; i < commentList.size(); i++){
        LocalDateTime oneWeek = commentList.get(i).getModifiedAt().plusDays(7);
        if(!realNow.isAfter(oneWeek)){
          commentList.remove(commentList.get(i));
        }
      }
      if(!commentList.isEmpty()){
        commentRepository.deleteAllInBatch(commentList);
      }
    }
  }





}
