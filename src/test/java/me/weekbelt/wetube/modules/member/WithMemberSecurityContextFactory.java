package me.weekbelt.wetube.modules.member;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.form.MemberJoinForm;
import me.weekbelt.wetube.modules.member.service.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithMemberSecurityContextFactory implements WithSecurityContextFactory<WithMember> {

    private final MemberService memberService;

    @Override
    public SecurityContext createSecurityContext(WithMember withMember) {
        String name = withMember.value();

        // 회원 가입
        MemberJoinForm memberJoinForm = new MemberJoinForm();
        memberJoinForm.setName(name);
        memberJoinForm.setEmail(name + "@hanmail.net");
        memberJoinForm.setPassword("12345678");
        memberJoinForm.setPassword2("12345678");
        memberService.processNewMember(memberJoinForm);

        // Authentication 만들고 SecurityContext에 넣어주기
        UserDetails principal = memberService.loadUserByUsername(memberJoinForm.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        return context;
    }
}
