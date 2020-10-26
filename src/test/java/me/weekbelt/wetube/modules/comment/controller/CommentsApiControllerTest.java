package me.weekbelt.wetube.modules.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.wetube.infra.MockMvcTest;
import me.weekbelt.wetube.modules.comment.Comment;
import me.weekbelt.wetube.modules.comment.CommentFactory;
import me.weekbelt.wetube.modules.comment.form.CommentCreateForm;
import me.weekbelt.wetube.modules.comment.form.CommentUpdateForm;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.WithMember;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import me.weekbelt.wetube.modules.video.Video;
import me.weekbelt.wetube.modules.video.VideoFactory;
import me.weekbelt.wetube.modules.video.form.VideoUploadForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class CommentsApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    VideoFactory videoFactory;
    @Autowired
    CommentFactory commentFactory;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @WithMember("joohyuk")
    @DisplayName("코멘트 추가 API - 성공")
    void createComment_success() throws Exception {
        // given
        Member member = memberRepository.findByName("joohyuk").get();
        VideoUploadForm videoUploadForm = VideoUploadForm.builder()
                .title("test video title")
                .description("test video description")
                // TODO: MockMultipartFile 재작성
                .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
                .build();
        Video video = videoFactory.createVideo(member, videoUploadForm);
        CommentCreateForm commentCreateForm = CommentCreateForm.builder()
                .text("test comment")
                .build();

        String requestUri = "/api/videos/" + video.getId() + "/comments";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentCreateForm))
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.videoId").value(video.getId()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.text").value(commentCreateForm.getText()))
                .andExpect(jsonPath("$.createdAt").exists());
    }


    @Test
    @WithMember("joohyuk")
    @DisplayName("코멘트 추가 API - 실패(내용 입력 x)")
    void createComment_fail_forNotText() throws Exception {
        // given
        Member member = memberRepository.findByName("joohyuk").get();
        VideoUploadForm videoUploadForm = VideoUploadForm.builder()
                .title("test video title")
                .description("test video description")
                // TODO: MockMultipartFile 재작성
                .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
                .build();
        Video video = videoFactory.createVideo(member, videoUploadForm);
        CommentCreateForm commentCreateForm = CommentCreateForm.builder()
                .text("")   // 내용 입력 x
                .build();

        String requestUri = "/api/videos/" + video.getId() + "/comments";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentCreateForm))
                .with(csrf()));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMember("joohyuk")
    @DisplayName("코멘트 리스트 불러오기")
    void commentList() throws Exception {
        // given
        Member member = memberRepository.findByName("joohyuk").get();
        VideoUploadForm videoUploadForm = VideoUploadForm.builder()
                .title("video test")
                .description("video description")
                .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
                .build();
        Video video = videoFactory.createVideo(member, videoUploadForm);

        for (int i = 1; i <= 36; i++) {
            CommentCreateForm commentCreateForm = CommentCreateForm.builder()
                    .text("comment test " + i)
                    .build();
            commentFactory.createComment(member, video, commentCreateForm);
        }

        // when
        String requestUri = "/api/videos/" + video.getId() + "/comments?page=1&sort=createdDate,desc";
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMember("joohyuk")
    @DisplayName("코멘트 수정 API - 성공")
    void modifyComment_success() throws Exception {
        // given
        Member member = memberRepository.findByName("joohyuk").get();
        VideoUploadForm videoUploadForm = VideoUploadForm.builder()
                .title("test video title")
                .description("test video description")
                // TODO: MockMultipartFile 재작성
                .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
                .build();
        Video video = videoFactory.createVideo(member, videoUploadForm);
        CommentCreateForm commentCreateForm = CommentCreateForm.builder()
                .text("test comment")
                .build();
        Comment comment = commentFactory.createComment(member, video, commentCreateForm);

        CommentUpdateForm commentUpdateForm = CommentUpdateForm.builder()
                .text("update test text")
                .build();

        String requestUri = "/api/videos/" + video.getId() + "/comments/" + comment.getId();

        // when
        ResultActions resultActions = mockMvc.perform(put(requestUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentUpdateForm))
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(comment.getId()))
                .andExpect(jsonPath("videoId").value(video.getId()))
                .andExpect(jsonPath("name").value(member.getName()))
                .andExpect(jsonPath("text").value(commentUpdateForm.getText()))
                .andExpect(jsonPath("createdAt").exists());
    }

    // TODO: 수정
//    @Test
//    @WithMember("joohyuk")
//    @DisplayName("코멘트 수정 API - 실패(내용 x)")
//    void modifyComment_fail() throws Exception {
//        // given
//
//        // member 호출
//        Member member = memberRepository.findByName("joohyuk").get();
//
//        // video 생성
//        VideoUploadForm videoUploadForm = VideoUploadForm.builder()
//                .title("test video title")
//                .description("test video description")
//                // TODO: MockMultipartFile 재작성
//                .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
//                .build();
//        Video video = videoFactory.createVideo(member, videoUploadForm);
//
//        // comment 생성
//        CommentCreateForm commentCreateForm = CommentCreateForm.builder()
//                .text("test comment")
//                .build();
//        Comment comment = commentFactory.createComment(member, video, commentCreateForm);
//
//        // 요청할 json객체
//        CommentUpdateForm commentUpdateForm = CommentUpdateForm.builder()
//                .text("aa") // 내용 입력 3자 이하
//                .build();
//
//        String requestUri = "/api/videos/" + video.getId() + "/comments/" + comment.getId();
//
//        // when
//        ResultActions resultActions = mockMvc.perform(put(requestUri)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(commentUpdateForm))
//                .with(csrf()));
//
//        // then
//        resultActions
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @WithMember("joohyuk")
    @DisplayName("코멘트 삭제")
    void removeComment() throws Exception {
        // given
        // member 호출
        Member member = memberRepository.findByName("joohyuk").get();

        // video 생성
        VideoUploadForm videoUploadForm = VideoUploadForm.builder()
                .title("test video title")
                .description("test video description")
                // TODO: MockMultipartFile 재작성
                .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
                .build();
        Video video = videoFactory.createVideo(member, videoUploadForm);

        // comment 생성
        CommentCreateForm commentCreateForm = CommentCreateForm.builder()
                .text("test comment")
                .build();
        Comment comment = commentFactory.createComment(member, video, commentCreateForm);

        String requestUri = "/api/videos/" + video.getId() + "/comments/" + comment.getId();

        // when
        ResultActions resultActions = mockMvc
                .perform(delete(requestUri).with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(comment.getId()))
                .andExpect(jsonPath("videoId").value(video.getId()))
                .andExpect(jsonPath("name").value(member.getName()))
                .andExpect(jsonPath("text").value(comment.getText()))
                .andExpect(jsonPath("createdAt").exists());

    }
}