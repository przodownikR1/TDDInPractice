package pl.java.scalatech.config;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthClientSettingValidator implements ConstraintValidator<AuthRestValid, RestClientSetting> {

    @Override
    public boolean isValid(RestClientSetting setting, ConstraintValidatorContext context) {
        if (!StringUtils.isEmpty(setting.getUser())) {
            return !StringUtils.isEmpty(setting.getPersonalToken());
        }
        return true;
    }

}
