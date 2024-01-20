// $(document).ready(function () {
//     fetchNotices();
//
//     function fetchNotices() {
//         var clubId = getClubId();
//
//         $.ajax({
//             type: 'GET',
//             url: '/api/club/' + clubId + '/notice',
//             success: function (data) {
//                 displayNotices(data);
//             },
//             error: function (error) {
//                 console.error('공지사항을 가져오는 중 에러 발생:', error);
//             }
//         });
//     }
//
//     function displayNotices(notices) {
//         var noticesList = $('<ul>');  // 공지사항을 목록으로 감싸기 위해 ul 열기
//
//         notices.forEach(function (notice) {
//             var noticeItem = $('<li>').addClass('notice-message').text(notice);  // 각각의 공지사항을 리스트 아이템으로 추가
//             noticesList.append(noticeItem);
//         });
//
//         // 목록을 Notice-container에 추가합니다.
//         $('.Notice-container').append(noticesList);
//     }
// });