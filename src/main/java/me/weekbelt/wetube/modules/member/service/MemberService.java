package me.weekbelt.wetube.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.wetube.modules.comment.Comment;
import me.weekbelt.wetube.modules.comment.CommentDtoFactory;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.comment.repository.CommentRepository;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.MemberDtoFactory;
import me.weekbelt.wetube.modules.member.Role;
import me.weekbelt.wetube.modules.member.UserMember;
import me.weekbelt.wetube.modules.member.form.*;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import me.weekbelt.wetube.modules.video.Video;
import me.weekbelt.wetube.modules.video.VideoDtoFactory;
import me.weekbelt.wetube.modules.video.form.VideoElementForm;
import me.weekbelt.wetube.modules.video.repository.VideoRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberReadForm findMemberWithVideosAndCommentsByName(String name) {
        Member member = memberRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 존재하지 않습니다. name=" + name));
        List<Video> videos = member.getVideos();
        List<Comment> comments = member.getComments();

        List<VideoElementForm> videoElementForms = VideoDtoFactory.videosToVideoElementForms(videos);
        List<CommentReadForm> commentReadForms = CommentDtoFactory.commentToCommentReadForms(comments);
        return MemberDtoFactory.memberToMemberReadForm(member, videoElementForms, commentReadForms);
    }

    public Member processNewMember(MemberJoinForm memberJoinForm) {
        return saveNewMember(memberJoinForm);
    }

    private Member saveNewMember(MemberJoinForm memberJoinForm) {
        Member member = MemberDtoFactory.memberJoinFormToMember(memberJoinForm, passwordEncoder);
        return memberRepository.save(member);
    }

    public void login(Member member) {
        Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(member);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserMember(member),
                member.getPassword(),
                grantedAuthorities);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Member member) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (member.getName().equals("weekbelt")) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }
        return grantedAuthorities;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 없습니다. email=" + email));
        return new UserMember(member);
    }

    public void updateProfile(Member member, MemberUpdateForm memberUpdateForm) {
        Member findMember = memberRepository.findByName(member.getName()).orElseThrow(
                () -> new UsernameNotFoundException("찾는 회원이 없습니다."));

        // TODO: 사진 로컬에 저장
        findMember.updateProfile(memberUpdateForm);
        memberRepository.save(findMember);
        login(findMember);
    }

    public void changeEmail(Member member, ChangeEmailForm changeEmailForm){
        Member findMember = memberRepository.findByName(member.getName()).orElseThrow(
                () -> new UsernameNotFoundException("찾는 회원이 없습니다."));

        findMember.changeEmail(changeEmailForm);
        memberRepository.save(findMember);
        login(findMember);
    }

    public void changePassword(Member member, ChangePasswordForm changePasswordForm) {
        Member findMember = memberRepository.findByName(member.getName()).orElseThrow(
                () -> new UsernameNotFoundException("찾는 회원이 없습니다."));

        findMember.changePassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        memberRepository.save(findMember);
    }
}
