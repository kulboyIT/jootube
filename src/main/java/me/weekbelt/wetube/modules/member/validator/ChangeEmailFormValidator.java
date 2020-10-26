package me.weekbelt.wetube.modules.member.validator;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.form.ChangeEmailForm;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChangeEmailFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangeEmailForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangeEmailForm changeEmailForm = (ChangeEmailForm) target;
        if (memberRepository.existsByEmail(changeEmailForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", "이미 사용중인 이메일 입니다.");
        }
    }
}
