package com.withus.withus.domain.chat.repository;

import com.withus.withus.domain.chat.entity.ChatRoom;
import com.withus.withus.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findChatRoomByIdAndIsActive(Long roomId, boolean isActive);

  @Query("SELECT cr FROM ChatRoom cr " +
      "WHERE ((cr.sender = :sender AND cr.receiver = :receiver) OR " +
      "       (cr.sender = :receiver AND cr.receiver = :sender)) " +
      "AND cr.isActive = :isActive")
  Optional<ChatRoom> findBySenderAndReceiverAndIsActive(
      @Param("sender") Member sender,
      @Param("receiver") Member receiver,
      @Param("isActive") boolean isActive
  );

  @Query("SELECT cr FROM ChatRoom cr " +
      "WHERE (cr.sender = :sender OR cr.receiver = :receiver) " +
      "AND cr.isActive = :isActive " +
      "ORDER BY cr.modifiedAt DESC")
  List<ChatRoom> findActiveChatRoomsBySenderOrReceiver(
      @Param("sender") Member sender,
      @Param("receiver") Member receiver,
      @Param("isActive") boolean isActive
  );

}
