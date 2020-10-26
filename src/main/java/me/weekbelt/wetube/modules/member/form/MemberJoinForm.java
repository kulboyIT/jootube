package me.weekbelt.wetube.modules.member.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class MemberJoinForm {
    @Email
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$", message = "아이디는 한글이나 영어로 3자에서 20자 사이로 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 20, message = "비밀번호를 8자에서 20자 사이로 입력해주세요.")
    private String password;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password2;

    @Override
    public String toString() {
        return "MemberJoinForm{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                '}';
    }
}
