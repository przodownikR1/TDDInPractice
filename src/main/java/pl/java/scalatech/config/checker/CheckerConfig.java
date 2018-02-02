package pl.java.scalatech.config.checker;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import static pl.java.scalatech.config.checker.TokenMaskTool.maskString;

import pl.java.scalatech.config.RestClientSetting;

@Configuration
@Profile("checker")
@Slf4j
public class CheckerConfig {
    @Bean
    CommandLineRunner runner(RestClientSetting settings, Environment environment) {
        return args -> printEnvAndProps(settings, environment);

    }

    private void printEnvAndProps(RestClientSetting settings, Environment environment) {
        log.info("env user : {}", environment.getProperty("rest-client.user"));
        log.info("env user token end on : {}", maskString(environment.getProperty("rest-client.personalToken"), 5, '#'));
        log.info("property user : {} ", settings.getUser());
        log.info("property token end on : {} ", maskString(environment.getProperty("rest-client.personalToken"), 5, '#'));
    }
}
