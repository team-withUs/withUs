package com.withus.withus.domain.notice.service;




import com.withus.withus.domain.club.entity.Club;

import com.withus.withus.domain.club.service.ClubMemberServiceImpl;
import com.withus.withus.domain.club.service.ClubServiceImpl;
import com.withus.withus.domain.comment.dto.CommentResponseDto;
import com.withus.withus.domain.notice.dto.PageableDto;
import com.withus.withus.domain.notice.dto.ReportRequestDto;
import com.withus.withus.domain.notice.entity.Notice;
import com.withus.withus.domain.notice.entity.NoticeCategory;
import com.withus.withus.domain.notice.entity.ReportNotice;
import com.withus.withus.global.response.exception.BisException;
import com.withus.withus.global.response.exception.ErrorCode;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.notice.dto.NoticeRequestDto;
import com.withus.withus.domain.notice.dto.NoticeResponseDto;
import com.withus.withus.domain.notice.repository.NoticeRepository;
import com.withus.withus.domain.notice.repository.ReportRepository;


import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final ReportRepository reportRepository;
    private final ClubServiceImpl clubService;
    private final ClubMemberServiceImpl clubMemberService;


    @Override
    public NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto, Member member) {
        if (!clubMemberService.existsClubMemberByMemberIdAndClubId(member.getId(), clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST);
        }
        Club club = clubService.findByIsActiveAndClubId(clubId);

        NoticeCategory category = NoticeCategory.BOARD;
        if (requestDto.category().equals("공지사항")) {
            category = NoticeCategory.NOTICE;
        }

        Notice createNotice = Notice.createNotice(requestDto, member, club, category);
        Notice saveNotice = noticeRepository.save(createNotice);
        return NoticeResponseDto.createNoticeResponseDto(saveNotice);
    }

    @Transactional
    @Override
    public NoticeResponseDto updateNotice(Long clubId, Long noticeId, NoticeRequestDto requestDto, Member member) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }

        Notice notice = findByIsActiveAndNoticeId(noticeId);
        if(clubMemberService.existHost(member.getId(), clubId) || notice.getMember().getId().equals(member.getId())){
            NoticeCategory category = NoticeCategory.BOARD;
            if (requestDto.category().equals("공지사항")) {
                category = NoticeCategory.NOTICE;
            }
            notice.update(requestDto, category);
            return NoticeResponseDto.createNoticeResponseDto(notice);
        }
        else {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

    }

    @Override
    public NoticeResponseDto getNotice(Long clubId, Long noticeId, Member member) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }
        Notice notice = findByIsActiveAndNoticeId(noticeId);
        if (!clubMemberService.existsClubMemberByMemberIdAndClubId(member.getId(), notice.getClub().getId())) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST);
        }
        return NoticeResponseDto.createNoticeResponseDto(notice);
    }

    @Override
    public Page<NoticeResponseDto> getsNotice(
            Long clubId,
            PageableDto pageableDto
    ) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }
        Page<Notice> noticePage = noticeRepository
                .findAllByIsActiveAndClubId(true,clubId, PageableDto.
                        getsPageableDto(
                                pageableDto.page(),
                                pageableDto.size(),
                                pageableDto.sortBy()
                        ).toPageable()
                );
        return noticePage.map(NoticeResponseDto::createNoticeResponseDto);
    }

    @Override
    public Integer count(Long clubId) {
        return noticeRepository.countByClubIdAndIsActive(clubId, true);
    }

    @Transactional
    @Override
    public void deleteNotice(Long clubId, Long noticeId, Member member) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }
        Notice notice = findByIsActiveAndNoticeId(noticeId);
        if(clubMemberService.existHost(member.getId(), clubId) || notice.getMember().getId().equals(member.getId())){
            notice.inActive();
        }
        else {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

    }

    @Transactional
    @Override
    public void createReportNotice(Long noticeId, ReportRequestDto requestDto, Member member) {
        Notice notice = findByIsActiveAndNoticeId(noticeId);
        if (!reportRepository.existsByNoticeIdAndMemberId(notice.getId(), member.getId())) {
            reportRepository.save(ReportNotice.createReport(requestDto, member, notice));
            if (reportRepository.countByNoticeId(notice.getId()) > 5) {
                notice.inActive();
            }
        } else {
            throw new BisException(ErrorCode.NOTICE_EXIST_REPORT);
        }

    }


    public Notice findByIsActiveAndNoticeId(Long noticeId) {
        Notice notice = noticeRepository.findByIsActiveAndId(true, noticeId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_NOTICE)
                );
        return notice;
    }

    public boolean existsByClubId(Long clubId) {
        return clubService.existByIsActiveAndClubId(clubId);
    }

    public boolean existByIsActiveAndNoticeId(Long noticeId) {
        return noticeRepository.existsByIsActiveAndId(true, noticeId);
    }
}
