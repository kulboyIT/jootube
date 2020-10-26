package me.weekbelt.wetube.modules.video.form;

import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class VideoUpdateForm {

    private String title;

    private String description;
}
