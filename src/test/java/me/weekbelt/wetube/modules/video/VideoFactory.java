package me.weekbelt.wetube.modules.video;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.video.form.VideoUploadForm;
import me.weekbelt.wetube.modules.video.repository.VideoRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoFactory {

    private final VideoRepository videoRepository;

    public Video createVideo(Member member, VideoUploadForm videoUploadForm) {
        Video video = Video.builder()
                .member(member)
                .title(videoUploadForm.getTitle())
                .description(videoUploadForm.getDescription())
                .views(0L)
                // TODO: video 처리
                .saveFileName(videoUploadForm.getFile().getOriginalFilename())
                .build();

        return videoRepository.save(video);
    }
}

