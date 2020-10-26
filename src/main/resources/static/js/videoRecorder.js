let streamObject;
let videoRecorder;

const videoRecorderObj = {
    init: function () {
        const recordBtn = document.getElementById("jsRecordBtn");
        recordBtn.addEventListener("click", this.getVideo);
    },
    getVideo: async function () {
        const videoPreview = document.getElementById("jsVideoPreview");
        const recordBtn = document.getElementById("jsRecordBtn");
        try {
            // 요청할 미디어 유형과 각각에 대한 요구사항 설정
            const stream = await navigator.mediaDevices.getUserMedia({
                audio: true,
                video: {width: 1280, height: 720}
            });
            videoPreview.srcObject = stream;
            videoPreview.muted = true;
            videoPreview.play();
            recordBtn.innerHTML = "Stop recording";

            streamObject = stream;
            videoRecorderObj.startRecording();
        } catch (e) {
            recordBtn.innerHTML = "☹️ Cant record";
        } finally {
            recordBtn.removeEventListener("click", videoRecorderObj.getVideo);
        }
    },
    startRecording: function () {
        videoRecorder = new MediaRecorder(streamObject);
        videoRecorder.start();
        videoRecorder.addEventListener("dataavailable", videoRecorderObj.handleVideoData);

        const recordBtn = document.getElementById("jsRecordBtn");
        recordBtn.addEventListener("click", videoRecorderObj.stopRecording);
    },
    handleVideoData: function (event) {
        const { data: videoFile } = event;
        const link = document.createElement("a");
        link.href = URL.createObjectURL(videoFile);
        link.download = "recorded.webm";
        document.body.appendChild(link);
        link.click();
    },
    stopRecording: function () {
        videoRecorder.stop();
        const recordBtn = document.getElementById("jsRecordBtn");
        recordBtn.removeEventListener("click", videoRecorderObj.stopRecording);
        recordBtn.addEventListener("click", videoRecorderObj.getVideo);
        recordBtn.innerHTML = "Start recording";
    }
};

document.addEventListener("DOMContentLoaded", () => {
    const recorderContainer = document.getElementById("jsRecordContainer");
    if (recorderContainer) {
        videoRecorderObj.init();
    }
});
