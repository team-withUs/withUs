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
        }, 10 * 1000*60);
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
          window.open('/api/chat/member/'+memberId+'/chatRoomListPage', 'blank');
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