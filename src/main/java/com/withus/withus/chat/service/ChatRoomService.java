package com.withus.withus.chat.service;

import com.withus.withus.chat.dto.ChatRoomPageListResponseDto;
import com.withus.withus.chat.entity.ChatRoom;
import com.withus.withus.chat.repository.ChatRoomRepository;
import com.withus.withus.chat.dto.ChatPostDto;
import com.withus.withus.chat.dto.ChatRoomResponseDto;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.response.PageInfo;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

  private final MemberServiceImpl memberService;

  private final ChatRoomRepository chatRoomRepository;

  public URI getOrCreateRoom(ChatPostDto chatPostDto, Member member) {
    if(member == null) {
      log.info("인증되지 않은 회원으로 채팅방을 생성할 수 없음");
      throw new BisException(ErrorCode.ACCESS_DENIED);
    }
    Member sender = memberService.findMemberByMemberId(member.getId());

    // 탈퇴한 회원 확인
    Member receiver = memberService.findMemberByMemberId(chatPostDto.id());

    // 자기 자신인지 검증
    if (sender.getId().equals(receiver.getId())) {
      log.info("자기 자신에게 채팅을 보낼수 없습니다.");
      throw new BisException(ErrorCode.REJECT_SEND_CHAT_SELF);
    }

    // 둘의 채팅이 있는 지 확인
    Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findBySenderAndReceiver(sender, receiver);
    Optional<ChatRoom> optionalChatRoom2 = chatRoomRepository.findBySenderAndReceiver(receiver, sender);

    ChatRoom chatRoom = null;

    if(optionalChatRoom.isPresent() || optionalChatRoom2.isPresent()) {
      chatRoom = optionalChatRoom.get();
      log.info("find chat room");

    } else {
      chatRoom = ChatRoom.createChatRomm(sender,receiver);
      log.info("create chat room");
      chatRoomRepository.save(chatRoom);
    }

    URI location = UriComponentsBuilder.newInstance()
        .path("ws/api/chat/{roomId}/message")
        .buildAndExpand(chatRoom.getId())
        .toUri();

    return location;
  }

  public ChatRoomResponseDto openChatRoom(Long roomId, Member member) {
    if (member == null) {
      log.info("인증되지 않은 회원으로 채팅방을 가져올 수 없음");
      throw new BisException(ErrorCode.ACCESS_DENIED);
    }
    ChatRoom chatRoom = findChatRoom(roomId);

    return ChatRoomResponseDto.createChatRoomResponseDto(
        chatRoom.getId(),
        chatRoom.getSender(),
        chatRoom.getReceiver()
    );
  }

  // 유저의 채팅 목록 가져오기
  public ChatRoomPageListResponseDto getsChatRoom(int page, int size, Member member) {
    if(member == null) {
      log.info("인증되지 않은 회원의 접근으로 채팅 목록을 가져올 수 없음");
      throw new BisException(ErrorCode.ACCESS_DENIED);
    }

    Member sender = memberService.findMemberByLoginname(member.getLoginname());
    Pageable pageable = PageRequest.of(page-1 , size, Sort.by("id").descending());
    Page<ChatRoom> chatRoomPage = chatRoomRepository.findAllBySenderOrReceiver(pageable, sender, sender);

    PageInfo pageInfo = new PageInfo(page, size, (int) chatRoomPage.getTotalElements(), chatRoomPage.getTotalPages());

    List<ChatRoom> chatRooms = chatRoomPage.getContent();
    List<ChatRoomResponseDto> chatRoomResponseDtos = chatRooms.stream()
        .map(chatRoom -> ChatRoomResponseDto.createChatRoomResponseDto(
            chatRoom.getId(),
            chatRoom.getSender(),
            chatRoom.getReceiver()
            )).toList();

    return ChatRoomPageListResponseDto.createChatPageListResponseDto(chatRoomResponseDtos, pageInfo);
  }

  // 채팅방 존재 검증
  public ChatRoom findChatRoom(Long roomId) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
        () -> new BisException(ErrorCode.NOT_FOUND_CHATROOM)
    );
    return chatRoom;
  }
}
