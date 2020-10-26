package me.weekbelt.wetube.modules.comment.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.comment.Comment;
import me.weekbelt.wetube.modules.comment.CommentDtoFactory;
import me.weekbelt.wetube.modules.comment.form.CommentCreateForm;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.comment.form.CommentUpdateForm;
import me.weekbelt.wetube.modules.comment.repository.CommentRepository;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import me.weekbelt.wetube.modules.video.Video;
import me.weekbelt.wetube.modules.video.repository.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;

    public CommentReadForm addComment(Long videoId, Long memberId, CommentCreateForm commentCreateForm) {
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 동영상이 존재하지 않습니다. videoId=" + videoId));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 회원이 존재하지 않습니다. memberId=" + memberId));

        Comment comment = CommentDtoFactory.commentCreateFormToComment(commentCreateForm, member, video);
        commentRepository.save(comment);

        return CommentDtoFactory.commentToCommentReadForm(comment);
    }

    public CommentReadForm modifyComment(Long commentId, CommentUpdateForm commentUpdateForm) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 코멘트가 존재하지 않습니다. commentId=" + commentId));
        comment.update(commentUpdateForm);

        return CommentDtoFactory.commentToCommentReadForm(comment);
    }

    public CommentReadForm removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 코멘트가 존재하지 않습니다. commentId=" + commentId));
        CommentReadForm commentReadForm = CommentDtoFactory.commentToCommentReadForm(comment);

        commentRepository.delete(comment);
        return commentReadForm;
    }

    public Page<CommentReadForm> getComments(Long videoId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAllByVideoIdOrderByCreatedDateDesc(videoId, pageable);
        return CommentDtoFactory.commentPageToCommentReadFormPage(commentPage);
    }
}
