package me.weekbelt.wetube.modules.comment.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CommentUpdateForm {

    @NotBlank
    @Length(min = 3, max = 200, message = "3자 이상 200자 미만으로 적어주세요.")
    private String text;
}
