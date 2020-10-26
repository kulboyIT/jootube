package me.weekbelt.wetube.modules.video.controller;

import lombok.With;
import me.weekbelt.wetube.infra.MockMvcTest;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.WithMember;
import me.weekbelt.wetube.modules.member.form.MemberJoinForm;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import me.weekbelt.wetube.modules.member.service.MemberService;
import me.weekbelt.wetube.modules.video.form.VideoUpdateForm;
import me.weekbelt.wetube.modules.video.form.VideoUploadForm;
import me.weekbelt.wetube.modules.video.service.VideoService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class VideoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @WithMember("joohyuk")
    @DisplayName("파일 업로드 화면")
    void uploadVideoPage() throws Exception {
        // given
        String requestUrl = "/videos/upload";

        // then
        ResultActions resultActions = mockMvc.perform(get(requestUrl));

        // when
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(model().attributeExists("videoUploadForm"))
                .andExpect(view().name("videos/upload"));
    }

    // TODO: 파일 업로드 테스트 구현
//    @Test
//    @WithMember("joohyuk")
//    void uploadVideo() throws Exception {
//        // given
//        Member member = memberRepository.findByName("joohyuk").orElse(null);
//
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("video", "video.mp4", "video/mp4", "video".getBytes());
//        // when
//        ResultActions resultActions = mockMvc.perform(multipart("/videos/upload").file(mockMultipartFile));
//
//        // then
//        resultActions
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/videos"));
//
//    }
}