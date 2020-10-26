package me.weekbelt.wetube.modules.member.validator;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.form.MemberJoinForm;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberJoinFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberJoinForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberJoinForm memberJoinForm = (MemberJoinForm) target;
        if (!memberJoinForm.getPassword().equals(memberJoinForm.getPassword2())) {
            errors.rejectValue("password", "wrong.value", "입력한 패스워드가 일치하지 않습니다.");
        }

        if (memberRepository.existsByEmail(memberJoinForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", "이미 사용중인 이메일 입니다.");
        }

        if (memberRepository.existsByName(memberJoinForm.getName())) {
            errors.rejectValue("name", "invalid.name", "이미 사용중인 이름입니다.");
        }

    }
}
