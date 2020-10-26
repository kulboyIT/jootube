package me.weekbelt.wetube.modules.video.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.video.form.VideoReadForm;
import me.weekbelt.wetube.modules.video.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoApiController {

    @Value("${property.video.url}")
    private String CLASS_PATH;

    private final VideoService videoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getVideoDetail(@PathVariable Long id) {
        VideoReadForm videoReadForm = videoService.findVideoForm(id);

        File fileVideo = new File(CLASS_PATH + videoReadForm.getSaveFileName());
        long fileLength = fileVideo.length();
        String saveFileName = CLASS_PATH + videoReadForm.getSaveFileName();
        String[] contentType = {"video/mp4", "video/webm"};

        return setResponseVideo(fileLength, saveFileName, contentType);
    }

    private ResponseEntity<?> setResponseVideo(long fileLength, String saveFileName, String[] contentType) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(new FileSystemResource(saveFileName));
    }
}
