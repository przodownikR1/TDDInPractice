package pl.java.scalatech.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = pl.java.scalatech.config.AuthClientSettingValidator.class)
public @interface AuthRestValid {
    String message() default "{auth client pairs property are invalid }";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

