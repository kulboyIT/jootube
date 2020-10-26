package me.weekbelt.wetube.modules.comment.form;

import lombok.*;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CommentReadForm {

    private Long id;

    private Long videoId;

    private String name;

    private String text;

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;

}
