$(document).ready(function () {
    var club = {};
    console.log(club)

    $("#imgBtn2").on("click", function () {
        createData();
        // window.location.href = '/api/club/main-club/'
    });
    function createData() {
        var formData = new FormData();
        formData.append("clubTitle", $("#clubTitleInput").val());
        formData.append("category", $(".btn-secondary.dropdown-toggle").text());
        formData.append("content", $("#club-content").val());
        formData.append("maxMember", $("#totalInviteesInput").val())
        formData.append("startTime", $("#startDate").val());
        formData.append("endTime", $("#endDate").val());

        var fileInput = document.getElementById('fileInput');
        console.log(fileInput.files);
        if (fileInput.files.length>0){
            formData.append("imageFile", fileInput.files[0]);
        }

        console.log("test",formData)
        $.ajax({
            type: "POST",
            url: "/api/club",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("데이터 저장 성공:", response);

                var imageURL = response.data.imageURL;
                console.log("서버에서 받은 imageURL:", imageURL);
                $("#uploadedImage").attr("src", imageURL);

                if (imageURL) {
                    $('.all-container').css('background-image', 'url(' + imageURL + ')');
                }
            },
            error: function (xhr, status, error) {
                // 에러 시 수행할 작업
                console.error("에러 상태:", status, "에러:", error);
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
            container.css('background-image', 'url(' + e.target.result + ')');
        };
        reader.readAsDataURL(input.files[0]);
    }
}
// 카테고리
$(document).ready(function () {
    $('#club-category li').on('click', function () {
        var selectedCategory = $(this).text();
        $('#dropdownMenuLink').text(selectedCategory);
    });
});