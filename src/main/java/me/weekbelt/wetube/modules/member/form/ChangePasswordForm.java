package me.weekbelt.wetube.modules.member.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ChangePasswordForm {

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 20, message = "비밀번호를 8자에서 20자 사이로 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 20, message = "비밀번호를 8자에서 20자 사이로 입력해주세요.")
    private String newPassword1;
}
