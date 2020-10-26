package me.weekbelt.wetube.modules.member.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class MemberUpdateForm {

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$", message = "아이디는 한글이나 영어로 3자에서 20자 사이로 입력해주세요.")
    private String name;

    private MultipartFile multipartImageProfile;
}
