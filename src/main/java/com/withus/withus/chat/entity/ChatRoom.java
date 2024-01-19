package com.withus.withus.chat.entity;

import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
  private Long id;

  @Column
  private String title;

  @Column
  private Boolean isActive = true;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  private Member sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  private Member receiver;

  @Builder
  private ChatRoom(Member sender, Member receiver) {
    this.sender = sender;
    this.receiver = receiver;
    this.title = sender.getUsername() + ", " + receiver.getUsername() + " 채팅방";
  }

  public static ChatRoom createChatRomm(Member sender, Member receiver) {
    return ChatRoom.builder()
        .sender(sender)
        .receiver(receiver)
        .build();

  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void chatRoomTransform() {
    this.isActive = false;
  }
}
