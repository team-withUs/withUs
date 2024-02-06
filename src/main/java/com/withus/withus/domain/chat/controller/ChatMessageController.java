package com.withus.withus.domain.chat.controller;

import com.withus.withus.domain.chat.dto.ChatMessageResponseDto;
import com.withus.withus.domain.chat.dto.MessageDto;
import com.withus.withus.domain.chat.service.ChatMessageService;
import com.withus.withus.domain.chat.service.ChatRoomService;
import com.withus.withus.domain.notification.service.NotificationService;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

  private final ChatRoomService chatRoomService;

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final NotificationService notificationService;

  //Client가 SEND할 수 있는 경로
  //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
  //대상 roomId를 구독하고있는 구독자 대상에게 메세지를 전달함
  //"/ws/send/api/chat/chatRoom/{roomId}/message"
  @MessageMapping("/api/chat/chatRoom/{roomId}/message")
  public void message(@DestinationVariable("roomId") Long roomId, MessageDto messageDto
  ) {
    simpMessagingTemplate.convertAndSend("/room/api/chat/chatRoom/" + roomId + "/message", messageDto);
    chatMessageService.saveMessage(roomId, messageDto);
  }

  @MessageMapping("/api/chat/chatRoom/{roomId}/message/enter")
  public void messageEnter(@DestinationVariable("roomId") Long roomId, MessageDto messageDto
  ) {
    simpMessagingTemplate.convertAndSend("/room/api/chat/chatRoom/" + roomId + "/message", messageDto);
    Long receiverId = chatRoomService.findReceiverId(roomId, messageDto.getSenderId());
    String sender = chatRoomService.findSender(roomId, messageDto.getSenderId());

    notificationService.notifyMessage(receiverId, sender);
  }

  @GetMapping("/api/chat/chatRoom/{roomId}/message")
  public ResponseEntity<CommonResponse<List<ChatMessageResponseDto>>> getMessage(
      @PathVariable("roomId") Long roomId
  ) {
    List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessageService.getsMessage(roomId);

    return ResponseEntity.status(ResponseCode.OK.getHttpStatus()).body(
        CommonResponse.of(ResponseCode.OK, chatMessageResponseDtoList));
  }

}


