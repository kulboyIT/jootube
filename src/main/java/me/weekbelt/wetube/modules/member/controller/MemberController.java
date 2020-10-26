package me.weekbelt.wetube.modules.member.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.CurrentMember;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.MemberDtoFactory;
import me.weekbelt.wetube.modules.member.form.ChangeEmailForm;
import me.weekbelt.wetube.modules.member.form.ChangePasswordForm;
import me.weekbelt.wetube.modules.member.form.MemberReadForm;
import me.weekbelt.wetube.modules.member.form.MemberUpdateForm;
import me.weekbelt.wetube.modules.member.service.MemberService;
import me.weekbelt.wetube.modules.member.validator.ChangeEmailFormValidator;
import me.weekbelt.wetube.modules.member.validator.ChangePasswordFormValidator;
import me.weekbelt.wetube.modules.member.validator.MemberUpdateFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberUpdateFormValidator memberUpdateFormValidator;
    private final ChangeEmailFormValidator changeEmailFormValidator;
    private final ChangePasswordFormValidator changePasswordFormValidator;

    @InitBinder("memberUpdateForm")
    public void memberUpdateFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberUpdateFormValidator);
    }

    @InitBinder("changeEmailForm")
    public void changeEmailFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(changeEmailFormValidator);
    }

    @InitBinder("changePasswordForm")
    public void changePasswordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(changePasswordFormValidator);
    }

    @GetMapping("/profile/{name}")
    public String userDetail(@CurrentMember Member member, @PathVariable String name, Model model) {
        MemberReadForm memberReadForm = memberService.findMemberWithVideosAndCommentsByName(name);
        model.addAttribute("memberReadForm", memberReadForm);
        model.addAttribute("pageTitle", "Member Detail");
        model.addAttribute("member", member);
        model.addAttribute("isOwner", name.equals(member.getName()));
        return "users/userDetail";
    }

    @GetMapping("/edit-profile")
    public String editProfileView(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        MemberUpdateForm memberUpdateForm = MemberDtoFactory.memberToMemberUpdateForm(member);
        model.addAttribute("memberUpdateForm", memberUpdateForm);
        model.addAttribute("pageTitle", "Edit Profile");
        return "users/editProfile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@CurrentMember Member member, @Valid MemberUpdateForm memberUpdateForm,
                              Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute("pageTitle", "Member Detail");
            model.addAttribute("member", member);
            return "users/editProfile";
        }
        memberService.updateProfile(member, memberUpdateForm);
        attributes.addFlashAttribute("message", "정보를 수정하였습니다.");
        return "redirect:/members/profile";
    }

    @GetMapping("/change-email")
    public String changeEmailView(@CurrentMember Member member, Model model) {
        ChangeEmailForm changeEmailForm = MemberDtoFactory.memberToChangeEmailForm(member);
        model.addAttribute("changeEmailForm", changeEmailForm);
        model.addAttribute("pageTitle", "Change Email");
        model.addAttribute("member", member);
        return "users/changeEmail";
    }

    @PostMapping("/change-email")
    public String changeEmail(@CurrentMember Member member, @Valid ChangeEmailForm changeEmailForm,
                            Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute("pageTitle", "Change Email");
            model.addAttribute("member", member);
            return "users/changeEmail";
        }
        memberService.changeEmail(member, changeEmailForm);
        attributes.addFlashAttribute("message", "이메일을 수정하였습니다.");
        return "redirect:/members/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordView(@CurrentMember Member member, Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        model.addAttribute("pageTitle", "Change Password");
        model.addAttribute("member", member);
        return "users/changePassword";
    }

   @PostMapping("/change-password")
    public String changePassword(@CurrentMember Member member, @Valid ChangePasswordForm changePasswordForm,
                                 Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("member", member);
            return "users/changePassword";
        }
        memberService.changePassword(member, changePasswordForm);
        attributes.addFlashAttribute("message", "패스워드를 변경하였습니다.");
        return "redirect:/members/profile";
   }
}
