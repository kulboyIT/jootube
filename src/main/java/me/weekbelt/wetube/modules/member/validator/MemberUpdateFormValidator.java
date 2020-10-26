package me.weekbelt.wetube.modules.member.validator;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.form.MemberUpdateForm;
import me.weekbelt.wetube.modules.member.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberUpdateFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberUpdateForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberUpdateForm memberUpdateForm = (MemberUpdateForm) target;

        if (memberRepository.existsByName(memberUpdateForm.getName())) {
            errors.rejectValue("name", "invalid.name", "이미 사용중인 이름입니다.");
        }
    }
}
