package com.withus.withus.domain.notification.controller;

import com.withus.withus.domain.notification.service.NotificationService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.domain.member.entity.Member;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
