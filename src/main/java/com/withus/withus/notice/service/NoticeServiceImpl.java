package com.withus.withus.notice.service;


import static com.withus.withus.global.s3.S3Const.S3_DIR_NOTICE;

import com.withus.withus.club.entity.Club;
import com.withus.withus.club.service.ClubMemberServiceImpl;
import com.withus.withus.club.service.ClubServiceImpl;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.s3.S3Util;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.dto.ReportRequestDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.entity.NoticeCategory;
import com.withus.withus.notice.entity.ReportNotice;
import com.withus.withus.notice.repository.NoticeRepository;
import com.withus.withus.notice.repository.ReportRepository;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final ReportRepository reportRepository;
    private final ClubServiceImpl clubService;
    private final ClubMemberServiceImpl clubMemberService;
    private final S3Util s3Util;


    @Override
    public NoticeResponseDto createNotice(Long clubId, NoticeRequestDto requestDto, Member member) {
        Club club = clubService.findByIsActiveAndClubId(clubId);
        if (!clubMemberService.existsClubMemberByMemberIdAndClubId(member.getId(), clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST);
        }

        NoticeCategory category = NoticeCategory.BOARD;
        if (requestDto.category().equals("공지사항")) {
            category = NoticeCategory.NOTICE;
        }

        Notice createNotice;
        if (requestDto.imageFile() == null) {
            createNotice = Notice.createNotice(requestDto, member, club, category);
        } else {
            String filename = s3Util.uploadFile(requestDto.imageFile(), S3_DIR_NOTICE);
            createNotice = Notice.createNoticePlusImage(requestDto, member, club, category,
                    s3Util.getFileURL(filename, S3_DIR_NOTICE), filename);
        }

        Notice saveNotice = noticeRepository.save(createNotice);
        return NoticeResponseDto.createNoticeResponseDto(saveNotice);
    }

    @Transactional
    @Override
    public NoticeResponseDto updateNotice(Long clubId, Long noticeId, NoticeRequestDto requestDto, Member member) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }

        if (!clubMemberService.existsClubMemberByMemberIdAndClubId(member.getId(), clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST);
        }

        Notice notice = findByIsActiveAndNoticeId(noticeId);

        NoticeCategory category = NoticeCategory.BOARD;
        if (requestDto.category().equals("공지사항")) {
            category = NoticeCategory.NOTICE;
        }

        if (requestDto.imageFile() != null) {
            if (notice.getFilename() != null) {
                s3Util.deleteFile(notice.getFilename(), S3_DIR_NOTICE);
            }
            String filename = s3Util.uploadFile(requestDto.imageFile(), S3_DIR_NOTICE);
            notice.updatePlusImage(requestDto, category,
                    s3Util.getFileURL(filename, S3_DIR_NOTICE), filename
            );
        } else {
            notice.update(requestDto, category);
        }

        return NoticeResponseDto.createNoticeResponseDto(notice);
    }

    @Override
    public NoticeResponseDto getNotice(Long clubId, Long noticeId) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }
        Notice notice = findByIsActiveAndNoticeId(noticeId);
        return NoticeResponseDto.createNoticeResponseDto(notice);
    }

    @Override
    public List<NoticeResponseDto> getsNotice(Long clubId, PageableDto pageableDto) {
        if (!existsByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }
        List<Notice> noticeList = noticeRepository
                .findAllByIsActive(true, PageableDto.
                        getsPageableDto(
                                pageableDto.page(),
                                pageableDto.size(),
                                pageableDto.sortBy()
                        ).toPageable()
                );
        List<NoticeResponseDto> responseDtoList = new ArrayList<>();
        for (Notice notice : noticeList) {
            responseDtoList.add(NoticeResponseDto.createNoticeResponseDto(notice));
        }
        return responseDtoList;
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
        if (!clubMemberService.existsClubMemberByMemberIdAndClubId(member.getId(), clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST);
        }
        Notice notice = findByIsActiveAndNoticeId(noticeId);
        notice.inActive();
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
