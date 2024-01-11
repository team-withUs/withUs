package com.withus.withus.chat.service;

import com.withus.withus.chat.dto.ChatMessagePageListResponseDto;
import com.withus.withus.chat.dto.MessageDto;
import com.withus.withus.chat.entity.ChatMessage;
import com.withus.withus.chat.entity.ChatRoom;
import com.withus.withus.chat.repository.ChatMessageRepository;
import com.withus.withus.chat.dto.ChatMessageResponseDto;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.response.PageInfo;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

  private final MemberServiceImpl memberService;

  private final ChatRoomService chatRoomService;

  private final ChatMessageRepository chatMessageRepository;

  public void saveMessage(Long roomId, MessageDto messageDto) {

    Member member = memberService.findMemberByMemberId(messageDto.getSenderId());
    ChatRoom chatRoom = chatRoomService.findChatRoom(roomId);

    ChatMessage chatMessage = ChatMessage
        .builder()
        .content(messageDto.getContent())
        .sender(member)
        .chatRoom(chatRoom)
        .sendTime(LocalDateTime.now())
        .build();

    chatMessageRepository.save(chatMessage);
    log.info("메세지 저장 완료");
  }

  public ChatMessagePageListResponseDto getsMessage(long roomId, int page, int size, Member member) {
    if (member == null) {
      log.info("인증되지 않은 회원의 접근으로 채팅 목록을 가져올 수 없음");
      throw new BisException(ErrorCode.ACCESS_DENIED);
    }

    // 해당 채팅방의 메세지를 가져와야 함
    Page<ChatMessage> messages = findMessages(roomId, page, size);
    PageInfo pageInfo = new PageInfo(page, size, (int) messages.getTotalElements(),
        messages.getTotalPages());

    List<ChatMessage> messageList = messages.getContent();
    List<ChatMessageResponseDto> messageResponses =
        messageList.stream().map(
            chatMessage -> ChatMessageResponseDto.createChatMessageResponseDto(
                chatMessage.getId(),
                chatMessage.getSender().getUsername(),
                chatMessage.getContent(),
                chatMessage.getSendTime())
            ).toList();

    return ChatMessagePageListResponseDto.createChatMessagePageListResponseDto(messageResponses,
        pageInfo);
  }

  public Page<ChatMessage> findMessages(Long roomId, int page, int size) {
    ChatRoom chatRoom = chatRoomService.findChatRoom(roomId);

    Pageable pageable = PageRequest.of(page-1, size, Sort.by("id").descending());
    Page<ChatMessage> messages = chatMessageRepository.findByChatRoom(pageable, chatRoom);

    return messages;
  }


}