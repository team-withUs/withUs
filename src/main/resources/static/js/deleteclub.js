$(document).ready(function () {
    var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));


    $("#deleteButton").on("click", function () {
        var confirmation = confirm("정말로 클럽을 삭제하시겠습니까?");

        if (confirmation) {

            $.ajax({
                type: "DELETE",
                url: "/api/club/" + clubId,
                success: function (response) {
                    console.log("클럽 삭제 성공:", response);

                    window.location.href = '/';
                },
                error: function (xhr, status, error) {
                    console.error("클럽 삭제 에러. 상태:", status, "에러:", error);
                    console.log("서버 응답:", xhr.responseText);
                }
            });
        }
    });
    $("#deleteButton").on("mouseover", function (e) {
        $("#deleteTooltip").css({
            top: e.pageY + 10,
            left: e.pageX + 10
        });
        $("#deleteTooltip").show();
    });

    $("#deleteButton").on("mouseout", function (e) {
        $("#deleteTooltip").hide();
    });
});