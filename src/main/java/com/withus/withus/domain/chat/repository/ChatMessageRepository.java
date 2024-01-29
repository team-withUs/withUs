package com.withus.withus.domain.chat.repository;

import com.withus.withus.domain.chat.entity.ChatMessage;
import com.withus.withus.domain.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findAllByChatRoomOrderBySendTimeAsc(ChatRoom chatRoom);
}
