package pl.java.scalatech.config;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AuthClientSettingValidatorTest {

    private static final Validator validator;

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        validator = config.buildValidatorFactory().getValidator();
        config.buildValidatorFactory().close();
    }

    @Test
    public void shouldValidWhenPropertiesAreOk() {
        // given
        String expectedUser = "przodownikR1";
        String expectedToken = "gitHubPrivateToken";
        RestClientSetting setting = createSampleRestClientSetting(expectedUser, expectedToken);
        assertThat(checkValidTest(setting)).isTrue();
    }

    @Test
    public void shouldInValidWhenOnlyUserIsSet() {
        // given
        String expectedUser = "przodownikR1";
        String expectedToken = null;
        RestClientSetting setting = createSampleRestClientSetting(expectedUser, expectedToken);
        assertThat(checkValidTest(setting)).isFalse();
    }

    @Test
    public void shouldValidWhenOnlyTokenIsSet() {
        // given
        String expectedUser = "";
        String expectedToken = "gitHubPrivateToken";
        RestClientSetting setting = createSampleRestClientSetting(expectedUser, expectedToken);
        assertThat(checkValidTest(setting)).isTrue();
    }

    @Test
    public void shouldValidWhenTokenAndUserIsNotSet() {
        // given
        String expectedUser = "";
        String expectedToken = "";
        RestClientSetting setting = createSampleRestClientSetting(expectedUser, expectedToken);
        assertThat(checkValidTest(setting)).isTrue();
    }

    private boolean checkValidTest(RestClientSetting setting) {
        Set<ConstraintViolation<RestClientSetting>> constraintViolations = validator.validate(setting);

        if (constraintViolations.size() > 0) {
            constraintViolations.forEach(AuthClientSettingValidatorTest::printError);
            return false;
        }
        return true;
    }

    private static void printError(ConstraintViolation<RestClientSetting> violation) {
        log.error("path : {} , message : {}", violation.getPropertyPath(), violation.getMessage());
    }

    private RestClientSetting createSampleRestClientSetting(String user, String token) {
        RestClientSetting setting = new RestClientSetting();
        setting.setUser(user);
        setting.setPersonalToken(token);
        return setting;
    }

}
