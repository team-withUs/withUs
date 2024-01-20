
function navigateToNoticeAddPage() {

    var clubId = Number(window.location.pathname.split('club/').pop().replace(/[^0-9]/g, ''));

    window.location.href = "/api/club/" + clubId + "/notice/noticeAddPage";
}