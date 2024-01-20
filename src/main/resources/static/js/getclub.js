// $(document).ready(function () {
//     var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));
//
//     $("#reviseButton").on("click", function () {
//
//         location.href = '/api/club/' + clubId + '/revise-club';
//     });
//
//     $.ajax({
//         type: "GET",
//         url: "/api/club/" + clubId,
//         success: function (response) {
//             console.log(response);
//
//             var club = response.data;
//             $(".club-title-name").text(club.clubTitle);
//             $(".club-introduce-box").text(club.content);
//             $(".start-time").text("Start Date: " + club.startTime);
//             $(".end-time").text("End Date: " + club.endTime);
//
//             var imageURL = club.imageURL;
//             $("#uploadedImage").attr("src", imageURL);
//
//             if (imageURL) {
//                 $('.all-container').css('background-image', 'url(' + imageURL + ')');
//             }
//         },
//         error: function (xhr, status, error) {
//             console.error("에러상태: " + status + ", 에러: " + error);
//             console.log("서버 응답:", xhr.responseText);
//         }
//     });
// });
//

$(document).ready(function () {
    var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));

    $("#reviseButton").on("click", function () {
        location.href = '/api/club/' + clubId + '/revise-club';
    });

    // 초대된 사용자 가져오기
    $.ajax({
        type: "GET",
        url: "/api/club/" + clubId + "/inviteList",
        success: function (response) {
            console.log(response);


            var invitedUsers = response.data;
            var inviteContainer = $(".invite-member");

            invitedUsers.forEach(function (username) {
                inviteContainer.append("<div class='invited-user'>" + username + "</div>");
            });
        },
        error: function (xhr, status, error) {
            console.error("에러상태: " + status + ", 에러: " + error);
            console.log("서버 응답:", xhr.responseText);
        }
    });

    // 클럽 정보 가져오기
    $.ajax({
        type: "GET",
        url: "/api/club/" + clubId,
        success: function (response) {
            console.log(response);

            var club = response.data;
            $(".club-title-name").text(club.clubTitle);
            $(".club-introduce-box").text(club.content);
            $(".start-time").text("Start Date: " + club.startTime);
            $(".end-time").text("End Date: " + club.endTime);

            var imageURL = club.imageURL;
            $("#uploadedImage").attr("src", imageURL);

            if (imageURL) {
                $('.all-container').css('background-image', 'url(' + imageURL + ')');
            }
        },
        error: function (xhr, status, error) {
            console.error("에러상태: " + status + ", 에러: " + error);
            console.log("서버 응답:", xhr.responseText);
        }
    });
});