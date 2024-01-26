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

            var invitedList = response.data;
            var inviteContainer = $(".invite-member");


            invitedList.forEach(function (InviteMemberResponseDto) {
                var memberId = InviteMemberResponseDto.id;
                var username = InviteMemberResponseDto.username;
                var imageURL = InviteMemberResponseDto.imageURL;
                var role = InviteMemberResponseDto.clubMemberRole;

                var inviteMemberDiv = $('<div class="memberDiv">')

                if (role === "HOST") {
                    var hostDiv = $('<div class="host">host</div>');
                } else {
                    var hostDiv = $('<div class="host">guest</div>');
                }
                inviteMemberDiv.append(hostDiv);

                if (imageURL === null) {
                    var iconDiv = $('<img src="/img/myInfo.png" alt="멤버 이미지">')
                } else {
                    var iconDiv = $('<img src="' + imageURL + '" alt="멤버 이미지">')
                }

                inviteMemberDiv.append(iconDiv);

                var usernameDiv = $('<div class="invited-user">' + username + '</div>');
                inviteMemberDiv.append(usernameDiv);

                inviteContainer.append(inviteMemberDiv);

                iconDiv.on('click', function () {
                    window.location.href = host + '/api/member/profilePage/' + memberId;

                })


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
    $(".leaveClubButton").on("click", function () {
        $.ajax({
            type: "DELETE",
            url: "/api/club/" + clubId + "/leave",
            success: function (response) {
                console.log(response);

                alert("클럽에서 탈퇴되었습니다.");
                location.reload();
            },
            error: function (xhr, status, error) {
                console.error("클럽 탈퇴 에러상태: " + status + ", 에러: " + error);
                console.log("서버 응답:", xhr.responseText);
                alert("클럽 이미 탈퇴한 회원입니다..");
            }
        });
    });
});
