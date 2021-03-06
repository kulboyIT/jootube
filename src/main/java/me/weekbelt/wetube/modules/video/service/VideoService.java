package me.weekbelt.wetube.modules.video.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.wetube.infra.util.VideoFile;
import me.weekbelt.wetube.modules.comment.Comment;
import me.weekbelt.wetube.modules.comment.CommentDtoFactory;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.MemberDtoFactory;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import me.weekbelt.wetube.modules.video.VideoDtoFactory;
import me.weekbelt.wetube.modules.member.form.Creator;
import me.weekbelt.wetube.modules.video.Video;
import me.weekbelt.wetube.modules.video.form.*;
import me.weekbelt.wetube.modules.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    @Value("${property.video.url}")
    private String PATH;
    @Value("${property.video.uploadFolder}")
    private String UPLOAD_VIDEO;

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;

    public List<VideoElementForm> findVideoElementForms() {
        List<Video> videoList = videoRepository.findAllByOrderByIdDesc();
        return VideoDtoFactory.videosToVideoElementForms(videoList);
    }

    public List<VideoElementForm> findVideoElementFormsByKeyword(String keyword) {
        List<Video> videoList = videoRepository.findAllByVideoKeyword(keyword);
        return VideoDtoFactory.videosToVideoElementForms(videoList);
    }

    public VideoReadForm findVideoForm(Long videoId) {
        Video findVideo = videoRepository.findWithMemberById(videoId).orElseThrow(
                () -> new IllegalArgumentException("?????? ???????????? ????????????. Video Id=" + videoId));
        // ????????? ??????
        findVideo.plusView();
        Member findMember = findVideo.getMember();
        List<Comment> comments = findVideo.getComments();

        Creator creator = MemberDtoFactory.memberToCreator(findMember);
        List<CommentReadForm> commentReadForms = comments.stream()
                .map(CommentDtoFactory::commentToCommentReadForm)
                .collect(Collectors.toList());

        return VideoDtoFactory.videoToVideoReadForm(findVideo, creator, commentReadForms);
    }

    public VideoReadForm uploadVideo(Member member, VideoUploadForm videoUploadForm) {
        Member findMember = memberRepository.findByName(member.getName()).orElseThrow(
                () -> new IllegalArgumentException("?????? ????????? ???????????? ????????????. ?????? ??????=" + member.getName()));

        // ????????? Video ??????
        String saveFileName = saveVideoAtLocal(videoUploadForm);

        // DB??? Video ??????
        Video video = VideoDtoFactory.videoUploadFormToVideo(videoUploadForm, findMember, saveFileName);

        Video savedVideo = videoRepository.save(video);
        Creator creator = MemberDtoFactory.memberToCreator(findMember);
        return VideoDtoFactory.videoToVideoReadForm(savedVideo, creator, new ArrayList<>());
    }

    private String saveVideoAtLocal(VideoUploadForm videoUploadForm) {
        MultipartFile videoMultipartFile = videoUploadForm.getFile();
        String fileName = VideoFile.makeInherenceFile(videoMultipartFile.getOriginalFilename());
        String saveFileName = UPLOAD_VIDEO + fileName;
        VideoFile.saveVideo(videoMultipartFile, PATH + saveFileName);
        return saveFileName;
    }

    public void updateVideo(Long videoId, VideoUpdateForm videoUpdateForm) {
        Video findVideo = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("???????????? ???????????? ????????????. video id=" + videoId));
        findVideo.update(videoUpdateForm);
    }

    public void deleteVideo(Long id) {
        Video findVideo = videoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("????????????????????? ???????????? ????????????. videoId=" + id));
        videoRepository.delete(findVideo);
    }

}
