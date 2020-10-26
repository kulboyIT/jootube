package me.weekbelt.wetube.modules.comment.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.comment.form.CommentCreateForm;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.comment.form.CommentUpdateForm;
import me.weekbelt.wetube.modules.comment.service.CommentService;
import me.weekbelt.wetube.modules.member.CurrentMember;
import me.weekbelt.wetube.modules.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/videos/{id}/comments")
@RequiredArgsConstructor
public class CommentsApiController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComments(@CurrentMember Member member,
                                         @RequestBody @Valid CommentCreateForm commentCreateForm,
                                         @PathVariable(name = "id") Long videoId, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        CommentReadForm commentReadForm = commentService.addComment(videoId, member.getId(), commentCreateForm);
        return ResponseEntity.ok(commentReadForm);
    }

    @GetMapping
    public ResponseEntity<?> comments(@PathVariable("id") Long videoId,
                                      @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentReadForm> comments = commentService.getComments(videoId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable Long commentId,
                                           @RequestBody @Valid CommentUpdateForm commentUpdateForm,
                                           Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        CommentReadForm commentReadForm = commentService.modifyComment(commentId, commentUpdateForm);
        return ResponseEntity.ok(commentReadForm);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<?> removeComment(@PathVariable Long commentId) {
        CommentReadForm commentReadForm = commentService.removeComment(commentId);
        return ResponseEntity.ok(commentReadForm);
    }
}
