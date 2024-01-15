package com.withus.withus.chat.controller;

import com.withus.withus.chat.dto.ChatPostDto;
import com.withus.withus.chat.dto.ChatRoomPageListResponseDto;
import com.withus.withus.chat.dto.ChatRoomResponseDto;
import com.withus.withus.chat.service.ChatRoomService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  // 채팅방 주소 가져오기
  @PostMapping("/api/chat")
  public ResponseEntity getOrCreateRoom(
      @Valid @RequestBody ChatPostDto chatPostDto,
      @AuthMember Member member
  ) {
    URI location = chatRoomService.getOrCreateRoom(chatPostDto, member);
    return ResponseEntity.created(location).build();
  }

  //  채팅방 열기
  @GetMapping("/api/chat/{roomId}")
  public ResponseEntity<CommonResponse<ChatRoomResponseDto>> getChatRoom(
      @PathVariable("roomId") Long roomId,
      @AuthMember Member member
  ) {
    ChatRoomResponseDto chatRoomResponseDto = chatRoomService.openChatRoom(roomId, member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, chatRoomResponseDto));
  }

  // 채팅방 목록 조회 -> 로그인한 유저가 참여하고 있는 채팅 목록
  @GetMapping("/api/chat")
  public ResponseEntity<CommonResponse<ChatRoomPageListResponseDto>> getsChatRoom(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @AuthMember Member member
  ) {
    ChatRoomPageListResponseDto chatRoomPageListResponseDto = chatRoomService.getsChatRoom(page, size, member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, chatRoomPageListResponseDto));
  }

  @DeleteMapping("/api/chat/{roomId}")
  public ResponseEntity<CommonResponse<String>> deleteChatRoom(
      @PathVariable("roomId") Long roomId,
      @AuthMember Member member
  ) {
    chatRoomService.deleteChatRoom(roomId, member);
    return ResponseEntity.ok().body(CommonResponse.of(ResponseCode.OK, ""));

  }

}