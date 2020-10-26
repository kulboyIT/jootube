const commentObj = {
    isValid: true,
    currentMemberName: document.querySelector("#currentMemberName").value,
    init: function () {
        // 댓글 작성 요청 이벤트 등록
        const commentCreateButton = document.querySelector("#commentAddBtn");
        commentCreateButton.addEventListener("click", this.createCommitRequest);
        // 댓글 시간 변환

    },
    createCommitRequest: async function () {
        const commentText = document.querySelector("#newCommentText");
        const createCommentForm = {
            text: commentText.value,
        }

        if (!commentObj.isValid) {
            alert("글자수를 3자 이상 200자 이하로 입력해주세요.");
        } else {
            const videoId = document.querySelector(".videoId").id;
            const requestUri = "/api/videos/" + videoId + "/comments";
            const response = await ajax("POST", requestUri, createCommentForm);

            if (response.name === commentObj.currentMemberName) {
                alert("등록 되었습니다.");
            }

            commentText.value = "";
            commentObj.addCommentTemplate(response);
        }
    },
    addCommentTemplate: function (commentReadForm) {
        // 최근 수정 시간을 ~전 으로 처리
        moment.locale('ko');
        commentReadForm.createdDateTime = moment(commentReadForm.createdDateTime, "YYYY-MM-DD`T`hh:mm").fromNow();
        commentReadForm.modifiedDateTime = moment(commentReadForm.modifiedDateTime, "YYYY-MM-DD`T`hh:mm").fromNow();

        if (commentReadForm.name === commentObj.currentMemberName) {
            commentReadForm.isWriter = true;
        }
        const commentElementTemplate = document.querySelector("#template").innerHTML;
        const bindTemplate = Handlebars.compile(commentElementTemplate);
        const commentElement = bindTemplate(commentReadForm);

        const commentListContainer = document.querySelector("#commentList");
        commentListContainer.insertAdjacentHTML("afterbegin", commentElement);
    }
};

document.addEventListener("DOMContentLoaded", () => {
    commentObj.init();
});