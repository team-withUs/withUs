$(document).ready(function () {
    var inviteeList = [];

    // 다크 모드와 라이트 모드 설정 부분
    var isDarkMode = localStorage.getItem('darkMode');
    if (isDarkMode === 'true') {
        $('body').addClass('dark-mode');
    }

    // 다크 모드와 라이트 모드를 토글하는 함수
    function toggleDarkMode() {
        $('body').toggleClass('dark-mode');
    }

    // 다크 모드 토글 버튼 또는 다른 요소에 이벤트 리스너 추가
    $('#darkModeToggle').on('click', function () {
        toggleDarkMode();

        // 다크 모드 상태를 localStorage에 저장
        var isDarkMode = $('body').hasClass('dark-mode');
        localStorage.setItem('darkMode', isDarkMode.toString());
    });

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
        var categoryKr = $(".btn-secondary.dropdown-toggle").text().trim();
        var category = getEnumValueForCategory(categoryKr);
        console.log(category)
        if (category.toLowerCase() === "") {
            alert("유효하지 않은 카테고리가 선택되었습니다. 유효한 카테고리를 선택해주세요.");
            return;
        }
        var content = $("#club-content").val().trim();
        var startTime = $("#startDate").val().trim();
        var endTime = $("#endDate").val().trim();

        if (!clubTitle || !category || !content || !startTime || !endTime) {
            alert("필수항목(제목, 카테고리, 내용, 시작시간, 마감시간)이 입력되지 않았습니다.");
            return;
        }

        if (new Date(endTime) < new Date(startTime)) {
            alert("마감시간은 시작시간보다 늦어야 합니다. 다시 설정해주세요.");
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
        });
    }

    $('#club-category li').on('click', function () {
        var selectedCategory = $(this).text();
        $('#dropdownMenuLink').text(selectedCategory);
        selectedCategory && inviteeList.push(selectedCategory);
    });
});

function handleFileSelect() {
    var input = document.getElementById('fileInput');
    var container = $('.all-container');
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            container.css({
                'background-image': 'url(' + e.target.result + ')',
                'background-size': 'cover',
            });
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function getEnumValueForCategory(categoryKr) {
    switch (categoryKr) {
        case "운동":
            return "SPORTS";
        case "일상":
            return "TODAY";
        case "게임":
            return "GAME";
        case "음식":
            return "FOOD";
        case "스터디":
            return "STUDY";
        case "기타":
            return "ETC";
        default:
            return "";
    }
}