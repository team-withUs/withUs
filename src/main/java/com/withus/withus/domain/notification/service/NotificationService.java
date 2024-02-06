package com.withus.withus.domain.notification.service;

import com.withus.withus.global.response.exception.BisException;
import com.withus.withus.global.response.exception.ErrorCode;
import com.withus.withus.domain.notification.controller.NotificationController;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

  public SseEmitter subscribe(Long memberId) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try{
      sseEmitter.send(SseEmitter.event().name("connect"));
    } catch (IOException e) {
      throw new BisException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    NotificationController.sseEmitters.put(memberId, sseEmitter);

    sseEmitter.onCompletion(()-> NotificationController.sseEmitters.remove(memberId));
    sseEmitter.onTimeout(()-> NotificationController.sseEmitters.remove(memberId));
    sseEmitter.onError((e)-> NotificationController.sseEmitters.remove(memberId));

    return sseEmitter;
  }

  public void notifyInviting(Long memberId, String clubTitle){

    if(NotificationController.sseEmitters.containsKey(memberId)) {
      SseEmitter sseEmitterReceiver = NotificationController.sseEmitters.get(memberId);

      try{
        sseEmitterReceiver.send(SseEmitter
            .event()
            .name("invitedClub")
            .data(clubTitle + "에 초대되었습니다.")
        );

      } catch (IOException e){
        NotificationController.sseEmitters.remove(memberId);
      }
    }

  }

  public void notifyMessage(Long receiverId, String sender){

    if(NotificationController.sseEmitters.containsKey(receiverId)){
      SseEmitter sseEmitterReceiver = NotificationController.sseEmitters.get(receiverId);

      try{
        sseEmitterReceiver.send(SseEmitter
            .event()
            .name("message")
            .data(sender+"님으로부터 채팅메세지가 도착했습니다."));

      } catch (IOException e){
        NotificationController.sseEmitters.remove(receiverId);
      }
    }

  }

}
