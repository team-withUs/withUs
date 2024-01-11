package com.withus.withus.chat.controller;

import com.withus.withus.chat.dto.ChatMessagePageListResponseDto;
import com.withus.withus.chat.dto.MessageDto;
import com.withus.withus.chat.service.ChatMessageService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

  private final SimpMessagingTemplate simpMessagingTemplate;


  @MessageMapping("/api/chat/message/{roomId}")
  public void message(@DestinationVariable("roomId") Long roomId, MessageDto messageDto) {
    simpMessagingTemplate.convertAndSend("/sub/api/chat/message/" + roomId, messageDto.getContent());
    chatMessageService.saveMessage(roomId, messageDto);
    log.info("Message [{}] send by member: {} to chatting room: {}", messageDto.getContent(), messageDto.getSenderId(), roomId);
  }

  // 채팅메세지 가져오기
  @GetMapping("/api/chat/message/{roomId}")
  public ResponseEntity<CommonResponse<ChatMessagePageListResponseDto>> getsMessages(
      @PathVariable("roomId") long roomId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @AuthMember Member member
  ) {

    ChatMessagePageListResponseDto chatMessagePageListResponseDto = chatMessageService.getsMessage(roomId, page, size, member);
    return ResponseEntity.ok().body(
        CommonResponse.of(ResponseCode.OK, chatMessagePageListResponseDto));
  }
}