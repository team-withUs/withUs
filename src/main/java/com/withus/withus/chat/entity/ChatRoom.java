package com.withus.withus.chat.entity;

import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private boolean isActive = true;

  @ManyToOne
  @JoinColumn(name = "sender_id")
  private Member sender;

  @ManyToOne
  @JoinColumn(name = "receiver_id")
  private Member receiver;

  @Builder
  private ChatRoom(Member sender, Member receiver) {
    this.sender = sender;
    this.receiver = receiver;
  }

  public static ChatRoom createChatRomm(Member sender, Member receiver) {
    return ChatRoom.builder()
        .sender(sender)
        .receiver(receiver)
        .build();
  }

  public void ChatRoomTransform() {
    this.isActive = !isActive;
  }
}
