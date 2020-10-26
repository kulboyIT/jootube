package me.weekbelt.wetube.modules.member.form;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ChangeEmailForm {

    @Email
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;
}
