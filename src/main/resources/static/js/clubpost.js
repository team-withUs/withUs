$(document).ready(function () {
    var inviteeList = [];

    $("#search-image").on("click", function () {
        loadMemberInfo();
    });

    $("#imgBtn2").on("click", function () {
        createDataAndConfirmInvitations();
    });

    $("#imgBtn3").on("click", function () {
        var userConfirmed = confirm("취소하시겠습니까?");
        if (userConfirmed) {
            alert("취소되었습니다.");
            window.location.href = '/';
        }
    });


    function loadMemberInfo() {
        var email = $("#searchInput").val();
        console.log("멤버 Email:", email);


        if (!email) {
            alert("이메일을 입력하세요.");
            return;
        }


        $.ajax({
            type: "GET",
            url: "/api/member/email/" + email,
            success: function (inviteeResponse) {
                var memberId = inviteeResponse.data.id;
                var email = inviteeResponse.data.email;

                updateInviteContainer(email);
            },
            error: function (xhr, status, error) {
                console.error("초대 대상 사용자 정보를 가져오는 중 (상태):", status, "에러:", error);
                console.log("서버 응답:", xhr.responseText);
                alert("사용자를 찾을 수 없습니다. 올바른 이메일을 입력해주세요.");
            }
        });
    }

    function updateInviteContainer(email) {
        $("#invite-container").append("<div>초대 이름: " + email + "</div>");

        inviteeList.push(email);
    }

    function createDataAndConfirmInvitations() {

        var clubTitle = $("#clubTitleInput").val().trim();
        var category = $(".btn-secondary.dropdown-toggle").text().trim();
        console.log(category)
        var content = $("#club-content").val().trim();
        // var maxMember = $("#totalInviteesInput").val().trim();
        var startTime = $("#startDate").val().trim();
        var endTime = $("#endDate").val().trim();

        if (!clubTitle || !category || !content || !startTime || !endTime) {
            alert("필수항목(제목, 카테고리, 내용, 시작시간, 마감시간)이 입력되지 않았습니다.");
            return;
        }

        var formData = new FormData();
        formData.append("clubTitle", clubTitle);
        formData.append("category", category);
        formData.append("content", content);
        // formData.append("maxMember", maxMember);
        formData.append("startTime", startTime);
        formData.append("endTime", endTime);

        var fileInput = document.getElementById('fileInput');
        if (fileInput.files.length > 0) {
            formData.append("imageFile", fileInput.files[0]);
        }

        $.ajax({
            type: "POST",
            url: "/api/club",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("데이터 저장 성공:", response);

                var clubId = response.data.clubId;
                console.log("클럽 ID:", clubId);

                console.log("최종 확인 버튼이 클릭되었습니다.");


                for (var i = 0; i < inviteeList.length; i++) {
                    var email = inviteeList[i];
                    console.log("초대를 보냅니다. 이메일:", email);

                    sendClubInvitation(email, clubId);
                }


                window.location.href = '/api/club/main-club/' + clubId;
            },
            error: function (xhr, status, error) {
                console.error("에러 상태:", status, "에러:", error);
                console.log("서버 응답:", xhr.responseText);
            }
        });
    }

    // 클럽 초대 보내기
    function sendClubInvitation(email, clubId) {
        $.ajax({
            type: "GET",
            url: "/api/member/email/" + email,
            success: function (inviteeResponse) {
                var memberId = inviteeResponse.data.id;

                console.log(memberId);
                var email = inviteeResponse.data.email;

                // 초대 보내기
                $.ajax({
                    type: "POST",
                    url: "/api/member/" + memberId + "/club/" + clubId,
                    success: function (response) {
                        console.log("초대 성공:", response);
                    },
                    error: function (xhr, status, error) {
                        console.error("초대 에러. 상태:", status, "에러:", error);
                        console.log("서버 응답:", xhr.responseText);
                    }
                });
            },
            error: function (xhr, status, error) {
                console.error("초대 대상 사용자 정보를 가져오는 중 (상태):", status, "에러:", error);
                console.log("서버 응답:", xhr.responseText);
                alert("사용자를 찾을 수 없습니다. 올바른 이메일을 입력해주세요.");
            }
        });
    }

    $('#club-category li').on('click', function () {
        var selectedCategory = $(this).text();
        $('#dropdownMenuLink').text(selectedCategory);
    });
});

function handleFileSelect() {
    var input = document.getElementById('fileInput');
    var container = $('.all-container');
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            container.css('background-image', 'url(' + e.target.result + ')');
        };
        reader.readAsDataURL(input.files[0]);
    }
}