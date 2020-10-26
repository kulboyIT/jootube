package me.weekbelt.wetube.modules.member.validator;

import lombok.RequiredArgsConstructor;
import me.weekbelt.wetube.modules.member.form.ChangePasswordForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChangePasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordForm changePasswordForm = (ChangePasswordForm) target;
        if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getNewPassword1())) {
            errors.rejectValue("newPassword", "wrong.value", "패스워드가 일치하지 않습니다.");
        }
    }
}
