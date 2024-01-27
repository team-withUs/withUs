$(document).ready(function () {
    var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));
    console.log(clubId)

    // 다크 모드와 라이트 모드 설정 부분
    var isDarkMode = localStorage.getItem('darkMode');
    if (isDarkMode === 'true') {
        $('body').addClass('dark-mode');
    }

    // 다크 모드와 라이트 모드를 토글하는 함수
    function toggleDarkMode() {
        $('body').toggleClass('dark-mode');
        // 추가로 스타일을 변경해야 하는 요소가 있다면 여기에 추가합니다.
    }

    // 다크 모드 토글 버튼 또는 다른 요소에 이벤트 리스너 추가
    $('#darkModeToggle').on('click', function () {
        toggleDarkMode();

        // 다크 모드 상태를 localStorage에 저장
        var isDarkMode = $('body').hasClass('dark-mode');
        localStorage.setItem('darkMode', isDarkMode.toString());
    });
    // 초대된 목록 초기화
    var inviteeList = [];

    $.ajax({
        type: "GET",
        url: "/api/club/" + clubId,
        success: function (response) {
            console.log(response);
            var club = response.data;

            $("#clubTitleInput").val(club.clubTitle);
            $(".btn-secondary.dropdown-toggle").text(club.category);
            $("#club-content").val(club.content);
            $("#startDate").val(club.startTime);
            $("#endDate").val(club.endTime);

            var imageURL = club.imageURL;
            $("#uploadedImage").attr("src", imageURL);

            if (imageURL) {
                $('.all-container').css({
                    'background-image': 'url(' + imageURL + ')',
                    'background-repeat': 'no-repeat',
                    'background-size': 'cover' // 바둑판 모양 방지 및 꽉 차게 표시
                });
            }

            // 초대된 목록 불러오기
            loadInviteeList();
        },
        error: function (xhr, status, error) {
            console.error("에러상태: " + status + ", 에러: " + error);
            console.log("서버 응답:", xhr.responseText);
        }
    });

    // 완료 버튼 눌렀을때 경로 이동
    $("#imgBtn2").on("click", function () {
        saveData();
        window.location.href = '/api/club/main-club/' + clubId;
    });

    $("#imgBtn3").on("click", function () {
        var userConfirmed = confirm("취소하시겠습니까?");
        if (userConfirmed) {
            alert("취소되었습니다.");
            window.location.href = '/api/club/main-club/' + clubId;
        }
    });

    $("#search-image").on("click", function () {
        inviteMember();
    });

    function inviteMember() {
        var email = $("#searchInput").val();
        console.log("멤버 Email:", email);

        $.ajax({
            type: "GET",
            url: "/api/member/email/" + email,
            success: function (inviteeResponse) {
                var memberId = inviteeResponse.data.id;

                console.log(memberId)
                var email = inviteeResponse.data.email;
                var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));

                // 초대 보내기
                $.ajax({
                    type: "POST",
                    url: "/api/member/" + memberId + "/club/" + clubId,
                    success: function (response) {
                        console.log("초대 성공:", response);
                        updateInviteContainer(email);
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
            }
        });
    }

    function updateInviteContainer(email) {
        $("#invite-container").append("<div>초대 이름: " + email + "</div>");
        // 초대된 목록에 추가
        inviteeList.push(email);
    }

    function loadInviteeList() {
        // 초대된 목록 불러와서 표시
        inviteeList.forEach(function (email) {
            updateInviteContainer(email);
        });
    }

    function saveData() {
        var formData = new FormData();
        formData.append("clubTitle", $("#clubTitleInput").val());
        formData.append("category", $(".btn-secondary.dropdown-toggle").text());
        formData.append("content", $("#club-content").val());
        formData.append("startTime", $("#startDate").val());
        formData.append("endTime", $("#endDate").val());

        var fileInput = document.getElementById('fileInput');
        console.log(fileInput.files);
        if (fileInput.files.length > 0) {
            formData.append("imageFile", fileInput.files[0]);
        } else {
            // 선택한 파일이 없을 때, 기존 이미지의 URL을 추가
            var imageURL = $("#uploadedImage").attr("src");
            formData.append("imageURL", imageURL);
        }

        console.log("test", formData)
        $.ajax({
            type: "PATCH",
            url: "/api/club/" + clubId,
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("데이터 저장 성공:", response);

                var imageURL = response.data.imageURL;
                console.log("서버에서 받은 imageURL:", imageURL);

                $("#uploadedImage").attr("src", imageURL);

                if (imageURL) {
                    $('.all-container').css({
                        'background-image': 'url(' + imageURL + ')',
                        'background-repeat': 'no-repeat',
                        'background-size': 'cover' // 바둑판 모양 방지 및 꽉 차게 표시
                    });
                }
                location.reload();
            },
            error: function (xhr, status, error) {
                console.error("데이터 저장 에러. 상태:", status, "에러:", error);
                console.log("서버 응답:", xhr.responseText);
            }
        });
    }
});

// 이미지
function handleFileSelect() {
    var input = document.getElementById('fileInput');
    var container = $('.all-container');

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            container.css({
                'background-image': 'url(' + e.target.result + ')',
                'background-size': 'cover'
            });
        };

        reader.readAsDataURL(input.files[0]);
    } else if (!input.value) {
        // 파일을 선택하지 않은 경우, 기존 이미지를 다시 불러옴
        var imageURL = $("#uploadedImage").attr("src");
        container.css({
            'background-image': 'url(' + imageURL + ')',
            'background-size': 'cover'
        });
    }
}

// 카테고리
$(document).ready(function () {
    $('#club-category li').on('click', function () {
        var selectedCategory = $(this).text();
        $('#dropdownMenuLink').text(selectedCategory);
    });
});
