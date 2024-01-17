package com.withus.withus.chat.controller;

import com.withus.withus.chat.dto.ChatRoomResponseDto;
import com.withus.withus.chat.service.ChatRoomService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final MemberServiceImpl memberService;

  // 채팅방 생성
  @PostMapping("/{memberId}")
  public ResponseEntity<CommonResponse<Long>> getOrCreateChatRoom(
      @PathVariable("memberId") Long memberId,
      @AuthMember Member member
  ) {
    Long roomId = chatRoomService.getOrCreateChatRoom(memberId, member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, roomId));
  }

  //  채팅방 상세 보기
  @GetMapping("/{roomId}")
  public ResponseEntity<CommonResponse<ChatRoomResponseDto>> getChatRoom(
      @PathVariable("roomId") Long roomId,
      @AuthMember Member member
  ) {
    member = memberService.findMemberByLoginname("junwoo");
    ChatRoomResponseDto chatRoomResponseDto = chatRoomService.getChatRoom(roomId, member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, chatRoomResponseDto));
  }

  // 채팅방 목록 조회 -> 로그인한 유저가 참여하고 있는 채팅 목록
  @GetMapping()
  public ResponseEntity<CommonResponse<List<ChatRoomResponseDto>>> getsChatRoom(
      @AuthMember Member member
  ) {

    List<ChatRoomResponseDto> chatRoomResponseDtoList = chatRoomService.getsChatRoom(member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, chatRoomResponseDtoList));
  }

  // 채팅방 나가기
  @DeleteMapping("/{roomId}")
  public ResponseEntity<CommonResponse<String>> deleteChatRoom(
      @PathVariable("roomId") Long roomId,
      @AuthMember Member member
  ) {

    chatRoomService.deleteChatRoom(roomId, member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, ""));
  }

}