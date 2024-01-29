function connectSse(){
  const eventSource = new EventSource(`/api/notification/subscribe`);
  eventSource.addEventListener("invitedClub", function (event) {
    console.log(event.data);
    const data = event.data;
    (async () => {
      //브라우저 알림
      const showNotification = () => {
        const notification = new Notification('알림', {
          body: data
        });

        setTimeout(() => {
          notification.close();
        }, 10*100*30);
      }

      let granted = false;
      if (Notification.permission === 'granted') {
        granted = true;
      } else if (Notification.permission !== 'denied') {
        let permission = await Notification.requestPermission();
        granted = permission === 'granted';
      }

      if (granted) {
        showNotification();
      }
    })();
  })

  eventSource.addEventListener("message", function (event) {
    console.log(event.data);
    const data = event.data;
    (async () => {
      //브라우저 알림
      const showNotification = () => {
        const notification = new Notification('알림', {
          body: data
        });

        setTimeout(() => {
          notification.close();
        }, 60 * 1000 * 60);

        notification.addEventListener('click', () => {
          notification.close();
          window.location.href='/api/chat/member/'+memberId+'/chatRoomListPage';
        });
      }

      let granted = false;
      if (Notification.permission === 'granted') {
        granted = true;
      } else if (Notification.permission !== 'denied') {
        let permission = await Notification.requestPermission();
        granted = permission === 'granted';
      }

      if (granted) {
        showNotification();
      }
    })();
  })
}