package com.withus.withus.club.service;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.club.entity.ReportClub;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.club.repository.ReportClubRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.s3.S3Const;
import com.withus.withus.global.s3.S3Util;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ReportClubRepository reportClubRepository;
    private final S3Util s3Util;
    private final ClubMemberService clubMemberService;


    @Override
    @Transactional
    public ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member, MultipartFile image) {
        LocalDateTime startTime = clubRequestDto.startTime();
        LocalDateTime endTime = clubRequestDto.endTime();
        try {
            if (StringUtils.isBlank(clubRequestDto.clubTitle())) {
                throw new BisException(ErrorCode.INVALID_VALUE);
            }
            if (startTime == null || endTime == null) {
                throw new BisException(ErrorCode.INVALID_VALUE);
            }
            String imageFile = null;

            if (image != null) {
                // S3에 이미지 업로드
                imageFile = s3Util.uploadFile(image, S3Const.S3_DIR_CLUB);
                System.out.println(imageFile);
            }
            Club club = Club.createClub(clubRequestDto, member, imageFile, startTime, endTime);
            if (imageFile != null) {
                club.setImageUrl(imageFile);
            }
            Club savedClub = clubRepository.save(club);
            ClubMember clubMember = ClubMember.createClubMember(club, member, ClubMemberRole.HOST);
            clubMemberService.createClubMember(clubMember);
            return ClubResponseDto.createClubResponseDto(savedClub);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BisException(ErrorCode.INVALID_VALUE);
        }
    }

    //조회
    @Override
    public ClubResponseDto getClub(Long clubId) {
        Club club = findClubById(clubId);
        return ClubResponseDto.createClubResponseDto(club);
    }

    /// 수정
    @Transactional
    @Override
    public ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member, MultipartFile image) {
        Club club = verifyMember(clubId);

        if (image != null) {
            try {
                String newImageFile = s3Util.uploadFile(image, S3Const.S3_DIR_CLUB);
                if (club.getImageUrl() != null) {
                    s3Util.deleteFile(club.getImageUrl(), S3Const.S3_DIR_CLUB);
                }
                club.setImageUrl(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BisException(ErrorCode.INVALID_VALUE);
            }
        } else {
            if (club.getImageUrl() != null) {
                s3Util.deleteFile(club.getImageUrl(), S3Const.S3_DIR_CLUB);
            }
            club.setImageUrl(null);
        }
        club.update(clubRequestDto, club.getFilename());
        return ClubResponseDto.createClubResponseDto(club);
    }

    @Override
    @Transactional
    public String deleteClub(Long clubId, Member member) {
        if (!existByClubId(clubId)) {
            throw new BisException(ErrorCode.NOT_FOUND_CLUB);
        }
        Club club = verifyMember(clubId);
        club.delete();
        return "Club delete successfully";
    }

    private boolean existByClubId(Long clubId) {
        return clubRepository.existsById(clubId);
    }

    @Override
    public ReportClubResponseDto createReportClub(Long clubId, ReportClubRequestDto reportClubRequestDto, Member member) {
        if (StringUtils.isBlank(reportClubRequestDto.content())) {
            throw new BisException(ErrorCode.INVALID_VALUE);
        }
        Club club = verifyMember(clubId);
        ReportClub reportClub = ReportClub.createReport(reportClubRequestDto, member, club);
        if (!reportClubRepository.existsByClubIdAndMemberId(club.getId(), member.getId())) {
            reportClubRepository.save(reportClub);
            if (reportClubRepository.countByClubId(club.getId()) > 5) {
                club.inActive();
            }
            return ReportClubResponseDto.createReportClubResponseDto(club, reportClub);
        } else {
            throw new BisException(ErrorCode.CLUB_EXIST_REPORT);
        }
    }

    @Override
    public List<ClubResponseDto> getsClubByCategory(ClubCategory category, PageableDto pageableDto) {
        List<Club> clubList;
        if(category.equals(ClubCategory.ALL)){
            clubList = clubRepository.
                findAllByIsActive(true, PageableDto.getsPageableDto(
                    pageableDto.page(),
                    pageableDto.size(),
                    pageableDto.sortBy()
                ).toPageable()
            );
        }
        else {
            clubList = clubRepository.
                findByCategoryAndIsActive(category, true, PageableDto.getsPageableDto(
                        pageableDto.page(),
                        pageableDto.size(),
                        pageableDto.sortBy()
                    ).toPageable()
                );
        }

        if (clubList == null || clubList.isEmpty()) {
            throw new BisException(ErrorCode.INVALID_VALUE);
        }
        return clubList.stream()
                .map(ClubResponseDto::createClubResponseDto)
                .collect(Collectors.toList());
    }

    public Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).
                orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CLUB));

    }

    private Club verifyMember(Long clubId) {
        Club club = clubRepository.findByIsActiveAndId(true, clubId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_CLUB)
                );
        return club;
    }

    public Club findByIsActiveAndClubId(Long clubId) {
        Club club = clubRepository.findByIsActiveAndId(true, clubId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_CLUB)
                );
        return club;
    }

    public boolean existByIsActiveAndClubId(Long clubId) {
        return clubRepository.existsByIsActiveAndId(true, clubId);
    }

    @Override
    public Integer count(){
        return clubRepository.countByIsActive(true);
    }
}
