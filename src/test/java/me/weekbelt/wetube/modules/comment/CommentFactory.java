package me.weekbelt.wetube.modules.comment;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.comment.form.CommentCreateForm;
import me.weekbelt.wetube.modules.comment.repository.CommentRepository;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.video.Video;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentFactory {

    private final CommentRepository commentRepository;

    public Comment createComment(Member member, Video video, CommentCreateForm commentCreateForm) {
        Comment comment = Comment.builder()
                .member(member)
                .video(video)
                .text(commentCreateForm.getText())
                .build();
        return commentRepository.save(comment);
    }
}
