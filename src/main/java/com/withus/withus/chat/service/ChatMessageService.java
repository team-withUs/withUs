package com.withus.withus.chat.service;

import com.withus.withus.chat.dto.ChatMessageResponseDto;
import com.withus.withus.chat.dto.MessageDto;
import com.withus.withus.chat.entity.ChatMessage;
import com.withus.withus.chat.entity.ChatRoom;
import com.withus.withus.chat.repository.ChatMessageRepository;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public List<ChatMessageResponseDto> getsMessage(long roomId) {

    List<ChatMessage> messageList = findMessages(roomId);

    List<ChatMessageResponseDto> messageResponseDtoList =
        messageList.stream().map(
            chatMessage -> ChatMessageResponseDto.createChatMessageResponseDto(
                chatMessage.getSender(),
                chatMessage.getContent(),
                chatMessage.getSendTime())
            ).toList();

    return messageResponseDtoList;
  }

  public List<ChatMessage> findMessages(Long roomId) {
    ChatRoom chatRoom = chatRoomService.findChatRoom(roomId);
    return chatMessageRepository.findAllByChatRoomOrderBySendTimeAsc(chatRoom);
  }

}