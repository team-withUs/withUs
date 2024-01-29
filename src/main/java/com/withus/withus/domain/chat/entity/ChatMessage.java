package com.withus.withus.domain.chat.entity;

import com.withus.withus.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private LocalDateTime sendTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  private Member sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private ChatRoom chatRoom;

  @Builder
  private ChatMessage(String content, LocalDateTime sendTime, Member sender, ChatRoom chatRoom) {
    this.content = content;
    this.sendTime = sendTime;
    this.sender = sender;
    this.chatRoom = chatRoom;
  }

  public static ChatMessage createChatMessage(
      String content,
      LocalDateTime sendTime,
      Member sender,
      ChatRoom chatRoom
  ) {
    return ChatMessage.builder()
        .content(content)
        .sendTime(sendTime)
        .sender(sender)
        .chatRoom(chatRoom)
        .build();
  }

  public void setMember(Member sender) {
    this.sender = sender;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setChatRoom(ChatRoom chatRoom) {
    this.chatRoom = chatRoom;
  }
}
