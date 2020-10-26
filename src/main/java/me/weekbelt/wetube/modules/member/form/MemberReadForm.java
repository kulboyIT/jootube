package me.weekbelt.wetube.modules.member.form;

import lombok.*;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.video.form.VideoElementForm;

import java.util.List;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class MemberReadForm {

    private String name;

    private String email;

    private String imageProfile;

    private List<VideoElementForm> videos;

    private List<CommentReadForm> comments;
}
