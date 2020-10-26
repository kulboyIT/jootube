package me.weekbelt.wetube.modules.video.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.weekbelt.wetube.modules.comment.form.CommentReadForm;
import me.weekbelt.wetube.modules.member.form.Creator;

import java.util.List;

@Getter @Setter
public class VideoReadForm {

    private Long id;
    private String title;
    private String description;
    private Long views;
    private String saveFileName;
    private Creator creator;
    private List<CommentReadForm> comments;

    @Builder
    public VideoReadForm(Long id, String title, String description,
                         Long views, String saveFileName,
                         Creator creator, List<CommentReadForm> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.views = views;
        this.saveFileName = saveFileName;
        this.creator = creator;
        this.comments = comments;
    }
}
