// $(document).ready(function () {
//     var clubId;
//
//
//     var inviteeList = [];
//     $("#imgBtn2").on("click", function () {
//         createData();
//     });
//
//     function createData() {
//         var formData = new FormData();
//         formData.append("clubTitle", $("#clubTitleInput").val());
//         formData.append("category", $(".btn-secondary.dropdown-toggle").text());
//         formData.append("content", $("#club-content").val());
//         formData.append("maxMember", $("#totalInviteesInput").val())
//         formData.append("startTime", $("#startDate").val());
//         formData.append("endTime", $("#endDate").val());
//
//         var fileInput = document.getElementById('fileInput');
//         if (fileInput.files.length > 0) {
//             formData.append("imageFile", fileInput.files[0]);
//         }
//
//         $.ajax({
//             type: "POST",
//             url: "/api/club",
//             data: formData,
//             processData: false,
//             contentType: false,
//             success: function (response) {
//                 console.log("데이터 저장 성공:", response);
//
//                 // 클럽 ID 얻어오기
//                 clubId = response.data.clubId;
//
//                 var imageURL = response.data.imageURL;
//                 console.log("서버에서 받은 imageURL:", imageURL);
//                 $("#uploadedImage").attr("src", imageURL);
//
//                 if (imageURL) {
//                     $('.all-container').css('background-image', 'url(' + imageURL + ')');
//                 }
//
//                 // 클럽 페이지로 이동
//                 if (clubId) {
//                     window.location.href = '/api/club/main-club/' + clubId;
//                 } else {
//                     console.error("clubId가 정의되지 않았거나 null입니다. 이동할 수 없습니다.");
//                 }
//             },
//             error: function (xhr, status, error) {
//                 // 에러 시 수행할 작업
//                 console.error("에러 상태:", status, "에러:", error);
//                 console.log("서버 응답:", xhr.responseText);
//             }
//         });
//     }
//
//     // 초대 기능
//     $("#search-image").on("click", function () {
//         inviteMember();
//     });
//
//     function inviteMember() {
//         var email = $("#searchInput").val();
//         console.log("멤버 Email:", email);
//
//         $.ajax({
//             type: "GET",
//             url: "/api/member/email/" + email,
//             success: function (inviteeResponse) {
//                 var memberId = inviteeResponse.data.id;
//
//                 console.log(memberId)
//                 var email = inviteeResponse.data.email;
//                 var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));
//
//                 // 초대 보내기
//                 $.ajax({
//                     type: "POST",
//                     url: "/api/member/" + memberId + "/club/" + clubId,
//                     success: function (response) {
//                         console.log("초대 성공:", response);
//                         updateInviteContainer(email);
//                     },
//                     error: function (xhr, status, error) {
//                         console.error("초대 에러. 상태:", status, "에러:", error);
//                         console.log("서버 응답:", xhr.responseText);
//                     }
//                 });
//             },
//             error: function (xhr, status, error) {
//                 console.error("초대 대상 사용자 정보를 가져오는 중 (상태):", status, "에러:", error);
//                 console.log("서버 응답:", xhr.responseText);
//                 alert("사용자를 찾을 수 없습니다. 올바른 이메일을 입력해주세요.");
//             }
//         });
//     }
//     function updateInviteContainer(email) {
//         $("#invite-container").append("<div>초대 이름: " + email + "</div>");
//         // 초대된 목록에 추가
//         inviteeList.push(email);
//     }
//
//     // 카테고리 선택 처리
//     $('#club-category li').on('click', function () {
//         var selectedCategory = $(this).text();
//         $('#dropdownMenuLink').text(selectedCategory);
//     });
// });
//
//
// // 이미지 업로드 처리
// function handleFileSelect() {
//     var input = document.getElementById('fileInput');
//     var container = $('.all-container');
//     if (input.files && input.files[0]) {
//         var reader = new FileReader();
//
//         reader.onload = function (e) {
//             container.css('background-image', 'url(' + e.target.result + ')');
//         };
//         reader.readAsDataURL(input.files[0]);
//     }
// }
$(document).ready(function () {
    var inviteeList = [];

    $("#imgBtn2").on("click", function () {
        createData();
    });

    function createData() {
        var formData = new FormData();
        formData.append("clubTitle", $("#clubTitleInput").val());
        formData.append("category", $(".btn-secondary.dropdown-toggle").text());
        formData.append("content", $("#club-content").val());
        formData.append("maxMember", $("#totalInviteesInput").val());
        formData.append("startTime", $("#startDate").val());
        formData.append("endTime", $("#endDate").val());

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

                var clubId = response.data.clubId; // 클럽 ID 얻어오기

                var imageURL = response.data.imageURL;
                console.log("서버에서 받은 imageURL:", imageURL);
                $("#uploadedImage").attr("src", imageURL);

                if (imageURL) {
                    $('.all-container').css('background-image', 'url(' + imageURL + ')');
                }

                // 클럽 페이지로 이동
                if (clubId) {
                    window.location.href = '/api/club/main-club/' + clubId;

                    inviteMember(clubId); // 클럽 ID를 이용해 초대
                } else {
                    console.error("clubId가 정의되지 않았거나 null입니다. 이동할 수 없습니다.");
                }
            },
            error: function (xhr, status, error) {
                console.error("에러 상태:", status, "에러:", error);
                console.log("서버 응답:", xhr.responseText);
            }
        });
    }

    // 초대 기능
    function inviteMember(clubId) {
        var email = $("#searchInput").val();
        console.log("멤버 Email:", email);

        $.ajax({
            type: "GET",
            url: "/api/member/email/" + email,
            success: function (inviteeResponse) {
                var memberId = inviteeResponse.data.id;

                console.log(memberId)
                var email = inviteeResponse.data.email;

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
                alert("사용자를 찾을 수 없습니다. 올바른 이메일을 입력해주세요.");
            }
        });
    }

    function updateInviteContainer(email) {
        $("#invite-container").append("<div>초대 이름: " + email + "</div>");
        // 초대된 목록에 추가
        inviteeList.push(email);
    }

    // 카테고리 선택 처리
    $('#club-category li').on('click', function () {
        var selectedCategory = $(this).text();
        $('#dropdownMenuLink').text(selectedCategory);
    });
});

// 이미지 업로드 처리
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
