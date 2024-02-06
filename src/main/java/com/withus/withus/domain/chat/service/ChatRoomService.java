package com.withus.withus.domain.chat.service;

import com.withus.withus.domain.chat.entity.ChatRoom;
import com.withus.withus.domain.chat.repository.ChatRoomRepository;
import com.withus.withus.domain.chat.dto.ChatRoomResponseDto;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.member.service.MemberServiceImpl;
import com.withus.withus.global.response.exception.BisException;
import com.withus.withus.global.response.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

  private final MemberServiceImpl memberService;

  private final ChatRoomRepository chatRoomRepository;

  public Long getOrCreateChatRoom(Long memberId, Member member) {
    Member sender = memberService.findMemberByMemberId(member.getId());

    // 탈퇴한 회원 확인
    Member receiver = memberService.findMemberByMemberId(memberId);

    // 자기 자신인지 검증
    if (sender.getId().equals(receiver.getId())) {
      log.info("자기 자신에게 채팅을 보낼수 없습니다.");
      throw new BisException(ErrorCode.REJECT_SEND_CHAT_SELF);
    }

    // 둘의 채팅이 있는 지 확인
    Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findBySenderAndReceiverAndIsActive(sender, receiver, true);
    Optional<ChatRoom> optionalChatRoom2 = chatRoomRepository.findBySenderAndReceiverAndIsActive(receiver, sender, true);

    ChatRoom chatRoom = null;

    if(optionalChatRoom.isPresent() || optionalChatRoom2.isPresent()) {
      chatRoom = optionalChatRoom.get();
      log.info("find chat room");

    } else {
      chatRoom = ChatRoom.createChatRoom(sender,receiver);
      log.info("create chat room");
      chatRoomRepository.save(chatRoom);
    }

    return chatRoom.getId();
  }

  public ChatRoomResponseDto getChatRoom(Long roomId, Member member) {

    memberService.findMemberByMemberId(member.getId());
    ChatRoom chatRoom = findChatRoom(roomId);

    return ChatRoomResponseDto.createChatRoomResponseDto(
        chatRoom.getId(),
        chatRoom.getTitle(),
        chatRoom.getSender(),
        chatRoom.getReceiver()
    );
  }

  // 유저의 채팅 목록 가져오기
  public List<ChatRoomResponseDto> getsChatRoom(Member member) {
    Member sender = memberService.findMemberByLoginname(member.getLoginname());


    List<ChatRoom> chatRoomList = chatRoomRepository.findActiveChatRoomsBySenderOrReceiver(sender, sender, true);

    List<ChatRoomResponseDto> chatRoomResponseDtoList = chatRoomList.stream()
        .map(chatRoom -> ChatRoomResponseDto.createChatRoomResponseDto(
            chatRoom.getId(),
            chatRoom.getTitle(),
            chatRoom.getSender(),
            chatRoom.getReceiver()
            ))
        .toList();

    return chatRoomResponseDtoList;
  }

  @Transactional
  public void deleteChatRoom(Long roomId, Member member) {
    ChatRoom chatRoom = findChatRoom(roomId);

    if (!(chatRoom.getSender().getId().equals(member.getId()) || chatRoom.getReceiver().getId().equals(member.getId()))) {
      throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
    }

    chatRoom.chatRoomTransform();
  }

  public Long findReceiverId(Long roomId, Long loginId ) {
    ChatRoom chatRoom = findChatRoom(roomId);
    Long senderId = chatRoom.getSender().getId();
    Long receiverId = chatRoom.getReceiver().getId();

    if (senderId.equals(loginId)) {
      return receiverId;
    } else {
      return senderId;
    }
  }

  public String findSender(Long roomId, Long loginId ) {
    ChatRoom chatRoom = findChatRoom(roomId);
    Long senderId = chatRoom.getSender().getId();
    Long receiverId = chatRoom.getReceiver().getId();

    if (senderId.equals(loginId)) {
      return memberService.findMemberByMemberId(senderId).getUsername();
    } else {
      return memberService.findMemberByMemberId(receiverId).getUsername();
    }
  }




  // 채팅방 존재 검증
  public ChatRoom findChatRoom(Long roomId) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdAndIsActive(roomId, true).orElseThrow(
        () -> new BisException(ErrorCode.NOT_FOUND_CHATROOM)
    );
    return chatRoom;
  }


}
