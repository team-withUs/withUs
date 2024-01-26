package com.withus.withus.chat.repository;

import com.withus.withus.chat.entity.ChatMessage;
import com.withus.withus.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findAllByChatRoomOrderBySendTimeAsc(ChatRoom chatRoom);
}
