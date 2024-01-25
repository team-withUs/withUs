package com.withus.withus.notification.controller;

import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notification.dto.NotifiationDto;
import com.withus.withus.notification.service.NotificationService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

  private final NotificationService notificationService;
  public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

  @GetMapping( "/subscribe")
  public SseEmitter notification(@AuthMember Member member) {
    SseEmitter sseEmitter = notificationService.subscribe(member.getId());

    return sseEmitter;
  }
}
