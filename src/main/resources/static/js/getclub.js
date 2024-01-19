$(document).ready(function () {
    var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));

    $("#reviseButton").on("click", function () {

        location.href = '/api/club/' + clubId + '/revise-club';
    });

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
            //클럽 참가자 정보
            $.ajax({
                type: "GET",

            })
        },
        error: function (xhr, status, error) {
            console.error("에러상태: " + status + ", 에러: " + error);
            console.log("서버 응답:", xhr.responseText);
        }
    });
});