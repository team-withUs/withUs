package com.withus.withus.notification.controller;

import com.withus.withus.notification.dto.NotifiationDto;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/notification")
  public void notification(@DestinationVariable("memberI") Long memberId) {
    messagingTemplate.convertAndSend("");
  }
}
