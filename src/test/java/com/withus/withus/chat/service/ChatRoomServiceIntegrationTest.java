package com.withus.withus.chat.service;

import static org.junit.jupiter.api.Assertions.*;

import com.withus.withus.chat.dto.ChatPostDto;
import com.withus.withus.chat.dto.ChatRoomPageListResponseDto;
import com.withus.withus.chat.dto.ChatRoomResponseDto;
import com.withus.withus.chat.entity.ChatRoom;
import com.withus.withus.chat.repository.ChatRoomRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import com.withus.withus.member.service.MemberServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DisplayName("채팅룸 서비스 통합 테스트")
class ChatRoomServiceIntegrationTest {

  @Autowired
  ChatRoomRepository chatRoomRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  ChatRoomService chatRoomService;

  @Autowired
  MemberServiceImpl memberService;

  Member loginMember;
  Member receiveMember;
  ChatPostDto chatPostDto;
  @BeforeEach
  void setup() {
    chatRoomService = new ChatRoomService(memberService, chatRoomRepository);

    receiveMember = memberRepository.save(Member.createMember(
        "testLogin1",
        "testPass1",
        "testEmail1@naver.com",
        "testUser1"
    ));
    loginMember = memberRepository.save(Member.createMember(
        "testLogin2",
        "testPass2",
        "testEmail2@naver.com",
        "testUser2"
    ));

  }

  @AfterEach
  void after() {
    memberRepository.deleteAll();


  }
  @Nested
  @DisplayName("채팅방 생성 or 가져오기 테스트")
  class getOrCreateRoomTest {

    @Test
    @DisplayName("성공")
    void getOrCreateRoomTest_success() {
      // given
      chatPostDto = new ChatPostDto(receiveMember.getId());

      // when
      chatRoomService.getOrCreateRoom(chatPostDto, loginMember);
      ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(loginMember, receiveMember).get();

      // then
      assertEquals(loginMember, chatRoom.getSender());
      assertEquals(receiveMember, chatRoom.getReceiver());
    }

    @Test
    @DisplayName("실패 - 본인에게 채팅을 보낼 경우")
    void getOrCreateRoomTest_sendChatSelf() {
      // given
      chatPostDto = new ChatPostDto(receiveMember.getId());

      // when - then
      assertThrows(BisException.class,
          () -> chatRoomService.getOrCreateRoom(chatPostDto, receiveMember));
    }

  }

  @Nested
  @DisplayName("채팅방 가져오기 테스트")
  class openChatRoomTest {

    @Test
    @DisplayName("성공")
    void openChatRoomTest_success() {
      // given
      ChatRoom chatRoom = ChatRoom.createChatRomm(loginMember, receiveMember);
      chatRoomRepository.save(chatRoom);

      // when
      ChatRoomResponseDto chatRoomResponseDto = chatRoomService.openChatRoom(chatRoom.getId(), loginMember);

      // then
      assertEquals(chatRoom.getId(), chatRoomResponseDto.roomId());
      assertEquals(loginMember.getUsername(), chatRoomResponseDto.sender());
      assertEquals(receiveMember.getUsername(), chatRoomResponseDto.receiver());
    }
  }

  //유저 채팅목록 가져오기 테스트 작성 paging..
  @Nested
  @DisplayName("채팅방 목록 가져오기 테스트")
  class getsChatRoomTest {

    @Test
    @DisplayName("채팅방 목록 가져오기 성공")
    void getsChatRoomTest_success() {
      // given
      Member receiveMember2 = memberRepository.save(Member.createMember(
          "testLogin3",
          "testPass3",
          "testEmail3@naver.com",
          "testUser3"
      ));
      ChatRoom chatRoom1 = chatRoomRepository.save(ChatRoom.createChatRomm(loginMember, receiveMember));
      ChatRoom chatRoom2 = chatRoomRepository.save(ChatRoom.createChatRomm(loginMember, receiveMember2));
      int page = 1;
      int size = 10;

      // when
      ChatRoomPageListResponseDto result = chatRoomService.getsChatRoom(page, size, loginMember);

      // then
      assertEquals(page, result.pageInfo().getPage());
      assertEquals(size, result.pageInfo().getSize());
      assertEquals(2, result.pageInfo().getTotalElements());
      assertEquals(1, result.pageInfo().getTotalPages());
      assertEquals(chatRoom2.getReceiver().getUsername(), result.chatRoomResponseDtos().get(0).receiver());
      assertEquals(chatRoom1.getReceiver().getUsername(), result.chatRoomResponseDtos().get(1).receiver());
    }
  }

  @Nested
  @DisplayName("채팅방 찾기 테스트")
  class findChatRoomTest {

    @Test
    @DisplayName("성공")
    void findChatRoomTest_success() {
      // given
      ChatRoom setChatRoom = ChatRoom.createChatRomm(loginMember, receiveMember);
      chatRoomRepository.save(setChatRoom);

      // when
      ChatRoom findChatRoom = chatRoomService.findChatRoom(setChatRoom.getId());

      // then
      assertEquals(setChatRoom.getId(), findChatRoom.getId());
      assertEquals(setChatRoom.getReceiver(), findChatRoom.getReceiver());
      assertEquals(setChatRoom.getSender(), findChatRoom.getSender());
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 채팅룸")
    void findChatRoomTest_NOTFOUNDCHATROOM() {
      // given

      // when - then
      assertThrows(BisException.class,
          () -> chatRoomService.findChatRoom(999L));

    }
  }

  @Nested
  @DisplayName("채팅방 비활성화 테스트")
  class deleteChatRoomTest {

    @Test
    @DisplayName("성공")
    void deleteChatRoomTest_success() {
      // given
      ChatRoom setChatRoom = ChatRoom.createChatRomm(loginMember, receiveMember);
      chatRoomRepository.save(setChatRoom);

      // when
      chatRoomService.deleteChatRoom(setChatRoom.getId(),loginMember);
      ChatRoom deactiveChatRoom = chatRoomRepository.findById(setChatRoom.getId()).get();

      // then
      assertEquals(false, deactiveChatRoom.isActive());
    }

    @Test
    @DisplayName("실패 - 당사자가 아닌 사람이 비활성화 시도 할 경우")
    void deleteChatRoomTest_YOURNOTCOME() {
      // given
      Member anotherMember = Member.createMember(
          "anotherlogin",
          "anotherpassword",
          "anotherEmail@naver.com",
          "anotheruser"
      );
      ChatRoom setChatRoom = ChatRoom.createChatRomm(loginMember, receiveMember);
      chatRoomRepository.save(setChatRoom);

      // when - then
      assertThrows(BisException.class,
          () -> chatRoomService.deleteChatRoom(setChatRoom.getId(),anotherMember));
    }
  }

}