package me.weekbelt.wetube.modules.video;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.wetube.modules.BaseTimeEntity;
import me.weekbelt.wetube.modules.comment.Comment;
import me.weekbelt.wetube.modules.member.Member;
import me.weekbelt.wetube.modules.video.form.VideoUpdateForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor
@Entity
public class Video extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Lob
    private String description;

    private Long views;

    private String saveFileName;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
    @org.hibernate.annotations.OrderBy(clause = "createdDate DESC")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Video(String title, String description, Long views, String saveFileName, Member member) {
        this.title = title;
        this.description = description;
        this.views = views;
        this.saveFileName = saveFileName;
        this.member = member;
    }

    public void addMember(Member member) {
        this.member = member;
        member.getVideos().add(this);
    }

    public void update(VideoUpdateForm videoUpdateForm) {
        this.title = videoUpdateForm.getTitle();
        this.description = videoUpdateForm.getDescription();
    }

    public void plusView() {
        this.views += 1;
    }
}
