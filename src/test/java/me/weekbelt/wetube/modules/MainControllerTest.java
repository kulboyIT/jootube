package me.weekbelt.wetube.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.wetube.infra.MockMvcTest;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.MemberFactory;
import me.weekbelt.wetube.modules.video.VideoFactory;
import me.weekbelt.wetube.modules.video.form.VideoUploadForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@MockMvcTest
class MainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberFactory memberFactory;

    @Autowired
    VideoFactory videoFactory;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("메인 페이지")
    void mainPage() throws Exception {
        // given
        String requestUrl = "/";

        // then
        ResultActions resultActions = mockMvc.perform(get(requestUrl));

        // then
        resultActions
                .andExpect(model().attributeExists("videos"))
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(view().name("home"));
    }

    @Test
    @DisplayName("검색 페이지")
    void searchVideo() throws Exception {
        // given
        createVideos();
        String requestUrl = "/search";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUrl)
                .param("keyword", "spring"));

        // then
        MockHttpServletResponse response = resultActions
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(model().attributeExists("searchingBy"))
                .andExpect(model().attributeExists("videos"))
                .andExpect(view().name("videos/search")).andReturn().getResponse();

        String contentAsString = response.getContentAsString();
        log.info("content: {}", contentAsString);
    }

    @Test
    @DisplayName("회원 가입 페이지")
    void joinPage() throws Exception {
        // given
        String requestUrl = "/join";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUrl));

        // then
        resultActions
                .andExpect(model().attributeExists("memberJoinForm"))
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(view().name("join"));
    }

    @Test
    @DisplayName("회원 가입 - 성공")
    void join_success() throws Exception {
        // given
        String requestUrl = "/join";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUrl)
                .param("email", "vfrvfr4207@hanmail.net")
                .param("name", "joohyuk")
                .param("password", "12345678")
                .param("password2", "12345678")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("joohyuk"));
    }


    @Test
    @DisplayName("회원 가입 - 실패(입력값 오류)")
    void join_fail() throws Exception {
        // given
        String requestUrl = "/join";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUrl)
                .param("name", "")
                .param("email", "vfrvfr4207@hanmail.net")
                .param("password", "12345678")
                .param("password2", "12345678")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("join"))
                .andExpect(model().hasErrors())
                .andExpect(unauthenticated());
    }


    @Test
    @DisplayName("회원 가입 - 실패(이미 존재하는 유저)")
    void join_fail_existsEmail() throws Exception {
        // given
        String requestUrl = "/join";
        Member member = memberFactory.createMember("joohyuk");

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUrl)
                .param("name", "joohyuk")
                .param("email", "vfrvfr4207@hanmail.net")
                .param("password", "12345678")
                .param("password2", "12345678")
                .with(csrf()));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("join"))
                .andExpect(model().hasErrors())
                .andExpect(unauthenticated());
    }


    private void createVideos() {
        Member member = memberFactory.createMember("joohyuk");
        IntStream.range(1, 11).forEach(i -> {
                    VideoUploadForm videoUploadForm = VideoUploadForm.builder()
                            .title("spring " + i)
                            .description("spring " + i + " description")
                            .file(new MockMultipartFile("video", "testVideo", "video/mp4", "testVideo".getBytes()))
                            .build();
                    videoFactory.createVideo(member, videoUploadForm);
                }
        );
    }
}