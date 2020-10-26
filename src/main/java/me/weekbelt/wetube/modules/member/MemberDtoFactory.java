package me.weekbelt.wetube.modules.member;

import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.member.form.*;
import me.weekbelt.wetube.modules.video.form.VideoElementForm;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class MemberDtoFactory {
    public static Creator memberToCreator(Member member) {
        return Creator.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    public static Member memberJoinFormToMember(MemberJoinForm memberJoinForm,
                                                PasswordEncoder passwordEncoder) {
        if (memberJoinForm.getName().equals("weekbelt")) {
            return Member.builder()
                    .email(memberJoinForm.getEmail())
                    .name(memberJoinForm.getName())
                    .password(passwordEncoder.encode(memberJoinForm.getPassword()))
                    .role(Role.ADMIN)
                    .build();
        } else {
            return Member.builder()
                    .email(memberJoinForm.getEmail())
                    .name(memberJoinForm.getName())
                    .password(passwordEncoder.encode(memberJoinForm.getPassword()))
                    .role(Role.USER)
                    .build();
        }
    }

    public static MemberUpdateForm memberToMemberUpdateForm(Member member) {
        // TODO: 프로파일 이미지 삽입
        return MemberUpdateForm.builder()
                .name(member.getName())
                .build();
    }

    public static ChangeEmailForm memberToChangeEmailForm(Member member) {
        return ChangeEmailForm.builder()
                .email(member.getEmail())
                .build();
    }

    public static MemberReadForm memberToMemberReadForm(Member member,
                                                        List<VideoElementForm> videos,
                                                        List<CommentReadForm> comments) {
        return MemberReadForm.builder()
                .email(member.getEmail())
                .name(member.getName())
                .imageProfile(member.getProfileImage())
                .videos(videos)
                .comments(comments)
                .build();
    }
}
