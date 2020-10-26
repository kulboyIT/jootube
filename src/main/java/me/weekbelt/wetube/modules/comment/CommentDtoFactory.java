package me.weekbelt.wetube.modules.comment;

import me.weekbelt.wetube.modules.comment.form.CommentCreateForm;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.video.Video;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDtoFactory {
    public static CommentReadForm commentToCommentReadForm(Comment comment) {
        return CommentReadForm.builder()
                .id(comment.getId())
                .videoId(comment.getVideo().getId())
                .name(comment.getMember().getName())
                .text(comment.getText())
                .createdDateTime(comment.getCreatedDate())
                .modifiedDateTime(comment.getLastModifiedDate())
                .build();
    }

    public static List<CommentReadForm> commentToCommentReadForms(List<Comment> comments) {
        return comments.stream().map(CommentDtoFactory::commentToCommentReadForm)
                .collect(Collectors.toList());
    }

    public static Comment commentCreateFormToComment(CommentCreateForm commentCreateForm,
                                                     Member member,
                                                     Video video) {
        return Comment.builder()
                .text(commentCreateForm.getText())
                .member(member)
                .video(video)
                .build();
    }

    public static Page<CommentReadForm> commentPageToCommentReadFormPage(Page<Comment> commentPage) {
        return commentPage.map(CommentDtoFactory::commentToCommentReadForm);
    }
}
