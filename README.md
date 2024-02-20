![image](https://github.com/team-withUs/withUs/assets/146637795/e877acbd-0b61-4b58-bcc8-40258db88dec)


# withUs
withUs는 취미를 공유하고 함께 즐길 수 있는 모임을 만들거나 가입할 수 있게 도와주는 사이트입니다!

축구를 하고싶은데 같이할 멤버가 없을때!

LoL를 같이할 멤버가 필요할때!

맛집 탐방을 다닐 멤버가 필요할때!

등 새로운 친구들을 만나서 취미를 공유해보세요!

### [withUs 바로가기](https://withus.life/)

---

## 아키텍처


![아키텍처](https://github.com/team-withUs/withUs/assets/146637795/6dd640e9-32fe-4801-a0d2-f9462d87569a)

---

## 주요 기술
+ WebSocket / Stomp를 이용한 1대1 채팅 기능
+ SSE를 사용한 알림 기능
+ Google SMTP를 활용한 이메일 인증
+ Redis를 활용한 Token 인증/인가
  
---

## 기술적 의사결정
<details>
  <summary>인증, 인가 방식</summary>
  <div>
   <li>세션 방식</li>
    <p>
      세션 방식은 임의의 회원의 세션을 삭제하는것으로 무효화가 가능하다는 점을 통해 보안측면에서 본다면 세션방식이 토큰방식보다 더 우수 하지만 scale-out시 
      추가적인 작업등이 필요하며 로그인한 회원이 많을수록 세션저장소의 리소스가 커져 서버의 부담이 크다는 단점이 존재
    </p>
  
    
  <li>토큰 방식</li>
    <p>
     토큰방식은 기 발급된 토큰을 임의로 무력화하는 등의 방법이 불가능하다는 한계점이 명확하여 세션보다 보안성이 떨어지지만 요청시 받은 토큰의 정보로만 인증이 이뤄지기 때문에 서버의 부담이 덜하다는 장점
     저희 프로젝트에서는 이용자의 이름, 전화번호 등과 같은 중요한 개인정보를 저장하지않기 때문에 다소 보안성이 떨어지더라도 서버의 부담이 적은 토큰방식을 채택하였으며 AccessToken과 RefeshToken을 사용하여 보안을 강화
    </p>
  
  </div>
</details>

<details>
  <summary>HTTP Polling VS WebSocket</summary>
  <div>
   <li>HTTP Polling</li>
    <p>
      HTTP Polling은 클라이언트가 지속적으로 서버에 요청을 보내고 응답을 받는 방식으로 양방향 통신은 가능하지만 사용자의 사용성 향상을 위해 요청의 간격이 짧게 설정할경우 서버의 부담이 늘어나 실시간 채팅기능에는 부적합하다고 생각
    </p>
  
    
  <li>WebSocket</li>
    <p>
     WebSocket은 Http프로토콜을 WebSocket프로토콜로 업그레이드하여 클라이언트와 서버간의 채널을 생성하여 HTTP 방식보다 구현난이도는 높지만 실시간채팅에 더 적합하다고 생각하여 WebSocket을 사용하였으며 
      추가적으로 1대1, 향후 다대다 채팅방으로 확장성을 고려해 STOMP프로토콜을 활용하여 구현
    </p>
  
  </div>
</details>

<details>
  <summary>알림 기능</summary>
  <div>
   <li>SSE</li>
    <p>
      마찬가지로 실시간성이 중요하므로 Web Socket으로 구현을 고려해봤으나 알림기능은 채팅기능과 달리 서버에서 일방적으로 클라이언트에게 데이터를 보내는 기능이므로 양방향 소통이 필요없다고 판단 
      또한 SSE는 WebSocket과 달리 HTTP 프로토콜을 사용하므로 구현이 비교적 쉽다는 장점도 있어서 SSE로 알림기능을 구현
    </p>
  </div>
</details>

---

## 멤버
+ [윤인석](https://github.com/dlstjr9390) : 회원 CRUD, 알림 기능, 프론트
+ [박승주](https://github.com/Joo-Veloper) : 클럽 CRUD, 프론트
+ [안준우](https://github.com/JunWoo0527) : 인증,인가 및 채팅 기능, 프론트
+ [전성훈](https://github.com/phantomrole) : 게시글 CRUD, 프론트
+ [김민석](https://github.com/CodeNameMS) : 댓글 CRUD, CI/CD
