const videoPlayerObj = {
    init: function () {
        // 동영상 uri 삽입
        this.createVideo();

        // 플레이 버튼 이벤트 등록
        const playBtn = document.getElementById("jsPlayButton");
        playBtn.addEventListener("click", this.handlePlayClick);

        // 음량 버튼 이벤트 등록
        const volumeBtn = document.getElementById("jsVolumeBtn");
        volumeBtn.addEventListener("click", this.handleVolumeClick);

        // 전체화면 or 일반화면 버튼 이벤트 등록
        const fullScreenBtn = document.getElementById("jsFullScreen");
        fullScreenBtn.addEventListener("click", this.goFullScreen);

        // 동영상 전체 시간 & 현재 시간표시 이벤트 등록
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        videoPlayer.volume = 0.5;
        videoPlayer.addEventListener("loadedmetadata", this.setTotalTime);
        videoPlayer.addEventListener("ended", this.handleEnded);

        // 볼륨바 이벤트 등록
        const volumeRange = document.getElementById("jsVolume");
        volumeRange.addEventListener("input", this.handleDrag);

    },
    createVideo: function () {
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        const videoId = document.querySelector(".videoId").id;
        videoPlayer.src = "/api/videos/" + videoId;
    },
    handlePlayClick: function () {
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        const playBtn = document.getElementById("jsPlayButton");
        if (videoPlayer.paused) {
            videoPlayer.play();
            playBtn.innerHTML = '<i class="fas fa-pause"></i>';
        } else {
            videoPlayer.pause();
            playBtn.innerHTML = '<i class="fas fa-play"></i>';
        }
    },
    handleVolumeClick: function () {
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        const volumeBtn = document.getElementById("jsVolumeBtn");
        const volumeRange = document.getElementById("jsVolume");
        if (videoPlayer.muted) {
            videoPlayer.muted = false;
            volumeBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
            volumeRange.value = videoPlayer.volume;
        } else {
            volumeRange.value = 0;
            videoPlayer.muted = true;
            volumeBtn.innerHTML = '<i class="fas fa-volume-mute"></i>';
        }
    },
    goFullScreen: function () {
        const videoContainer = document.getElementById("jsVideoPlayer");
        videoContainer.requestFullscreen().then(() => {
            const fullScreenBtn = document.getElementById("jsFullScreen");
            fullScreenBtn.innerHTML = '<i class="fas fa-compress"></i>';
            fullScreenBtn.removeEventListener("click", videoPlayerObj.goFullScreen);
            fullScreenBtn.addEventListener("click", videoPlayerObj.exitFullScreen);
        });
    },
    exitFullScreen: function () {
        const fullScreenBtn = document.getElementById("jsFullScreen");
        fullScreenBtn.innerHTML = '<i class="fas fa-expand"></i>';
        fullScreenBtn.addEventListener("click", videoPlayerObj.goFullScreen);
        document.webkitExitFullscreen();
    },
    setTotalTime: function () {
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        const totalTimeString = videoPlayerObj.formatDate(videoPlayer.duration);
        const totalTime = document.getElementById("totalTime");
        totalTime.innerHTML = totalTimeString;
        setInterval(videoPlayerObj.getCurrentTime, 1000);

    },
    formatDate: function (seconds) {
        const secondsNumber = parseInt(seconds, 10);
        let hours = Math.floor(secondsNumber / 3600);
        let minutes = Math.floor((secondsNumber - hours * 3600) / 60);
        let totalSeconds = secondsNumber - hours * 3600 - minutes * 60;

        if (hours < 10) {
            hours = `0${hours}`;
        }
        if (minutes < 10) {
            minutes = `0${minutes}`;
        }
        if (totalSeconds < 10) {
            totalSeconds = `0${totalSeconds}`;
        }
        return `${hours}:${minutes}:${totalSeconds}`;
    },
    getCurrentTime: function () {
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        const currentTime = document.getElementById("currentTime");
        currentTime.innerHTML = videoPlayerObj.formatDate(Math.floor(videoPlayer.currentTime));
    },
    handleEnded: function () {
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        videoPlayer.currentTime = 0;
        const playBtn = document.getElementById("jsPlayButton");
        playBtn.innerHTML = '<i class="fas fa-play"></i>';
    },
    handleDrag: function (event) {
        const {
            target: { value }
        } = event;
        const videoPlayer = document.querySelector("#jsVideoPlayer video");
        videoPlayer.volume = value;
        const volumeBtn = document.getElementById("jsVolumeBtn");
        if (value >= 0.6) {
            volumeBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
        } else if (value >= 0.2) {
            volumeBtn.innerHTML = '<i class="fas fa-volume-down"></i>';
        } else {
            volumeBtn.innerHTML = '<i class="fas fa-volume-off"></i>';
        }
    }
};

document.addEventListener("DOMContentLoaded", () => {
    const videoContainer = document.getElementById("jsVideoPlayer");
    if (videoContainer) {
        videoPlayerObj.init();
    }
});
