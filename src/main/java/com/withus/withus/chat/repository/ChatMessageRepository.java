package com.withus.withus.chat.repository;

import com.withus.withus.chat.entity.ChatMessage;
import com.withus.withus.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);
}
