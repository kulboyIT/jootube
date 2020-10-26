package me.weekbelt.wetube.modules.video.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.wetube.modules.member.CurrentMember;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.video.Video;
import me.weekbelt.wetube.modules.video.VideoDtoFactory;
import me.weekbelt.wetube.modules.video.form.VideoReadForm;
import me.weekbelt.wetube.modules.video.form.VideoUpdateForm;
import me.weekbelt.wetube.modules.video.form.VideoUploadForm;
import me.weekbelt.wetube.modules.video.repository.VideoRepository;
import me.weekbelt.wetube.modules.video.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/upload")
    public String uploadVideoForm(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        model.addAttribute("pageTitle", "Upload");
        model.addAttribute("videoUploadForm", new VideoUploadForm());
        return "videos/upload";
    }

    @PostMapping("/upload")
    public String uploadVideo(@CurrentMember Member member, VideoUploadForm videoUploadForm) {
        VideoReadForm videoReadForm = videoService.uploadVideo(member, videoUploadForm);
        return "redirect:/videos/" + videoReadForm.getId();
    }

    @GetMapping("/{id}")
    public String videoDetail(@CurrentMember Member member, @PathVariable Long id, Model model) {
        VideoReadForm videoReadForm = videoService.findVideoForm(id);
        model.addAttribute("video", videoReadForm);
        model.addAttribute("member", member);
        model.addAttribute("pageTitle", videoReadForm.getTitle());
        return "videos/videoDetail";
    }

    @GetMapping("/{id}/edit")
    public String editVideoView(@CurrentMember Member member, @PathVariable("id") Video video,
                                Model model, RedirectAttributes attributes) {
        VideoUpdateForm videoUpdateForm = VideoDtoFactory.videoToVideoUpdateForm(video);
        if (!member.getId().equals(video.getMember().getId())) {
            attributes.addFlashAttribute("message", "수정 권한이 없습니다.");
            return "redirect:/videos/" + video.getId();
        }
        model.addAttribute("id", video.getId());
        model.addAttribute("pageTitle", "Edit Video");
        model.addAttribute("videoUpdateForm", videoUpdateForm);
        model.addAttribute("member", member);
        return "videos/editVideo";
    }

    @PutMapping("/{id}/edit")
    public String editVideo(@CurrentMember Member member, @PathVariable Long id,
                            VideoUpdateForm videoUpdateForm, Model model) {
        videoService.updateVideo(id, videoUpdateForm);
        return "redirect:/videos/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteVideo(@CurrentMember Member member, @PathVariable("id") Video video,
                              RedirectAttributes attributes) {
        if (!member.getId().equals(video.getMember().getId())) {
            attributes.addFlashAttribute("message", "삭제 권한이 없습니다.");
            return "redirect:/videos/" + video.getId();
        }

        videoService.deleteVideo(video.getId());
        attributes.addFlashAttribute("message", "영상을 삭제 하였습니다.");
        return "redirect:/";
    }
}
