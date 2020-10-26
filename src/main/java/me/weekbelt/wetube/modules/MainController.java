package me.weekbelt.wetube.modules;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.wetube.modules.member.CurrentMember;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.form.MemberJoinForm;
import me.weekbelt.wetube.modules.member.service.MemberService;
import me.weekbelt.wetube.modules.member.validator.MemberJoinFormValidator;
import me.weekbelt.wetube.modules.video.form.VideoElementForm;
import me.weekbelt.wetube.modules.video.service.VideoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final VideoService videoService;
    private final MemberJoinFormValidator memberJoinFormValidator;

    @InitBinder("memberJoinForm")
    public void memberJoinFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberJoinFormValidator);
    }

    @GetMapping("/")
    public String mainPage(@CurrentMember Member member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        List<VideoElementForm> videos = videoService.findVideoElementForms();
        model.addAttribute("videos", videos);
        model.addAttribute("pageTitle", "Home");
        return "home";
    }

    @GetMapping("/search")
    public String searchVideo(@RequestParam String keyword, Model model) {
        model.addAttribute("pageTitle", "Search");
        model.addAttribute("searchingBy", keyword);
        List<VideoElementForm> videos = videoService.findVideoElementFormsByKeyword(keyword);
        model.addAttribute("videos", videos);
        return "videos/search";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("memberJoinForm", new MemberJoinForm());
        model.addAttribute("pageTitle", "Join");
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberJoinForm memberJoinForm, Errors errors,
                       RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            return "join";
        }

        Member member = memberService.processNewMember(memberJoinForm);
        memberService.login(member);
        attributes.addFlashAttribute("message", "회원가입에 성공하였습니다.");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Log In");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
