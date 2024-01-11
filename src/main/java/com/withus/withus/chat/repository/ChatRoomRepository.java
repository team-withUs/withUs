package com.withus.withus.chat.repository;

import com.withus.withus.chat.entity.ChatRoom;
import com.withus.withus.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findBySenderAndReceiver(Member sender, Member receiver);
  Page<ChatRoom> findAllBySenderOrReceiver(Pageable pageable, Member sender, Member receiver);
}
