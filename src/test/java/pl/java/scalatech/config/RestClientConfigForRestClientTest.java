package pl.java.scalatech.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sławomir Borowiec
 *         Module name : TDDInPractice
 *         Creating time : Feb 2, 2018 1:32:55 PM
 *         Snicoll spring boot 2.0.0 issue :
 * @PropertySource and TestPropertySource do not work with YAML indeed.
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-shortClient.properties")
@ContextConfiguration(classes = { RestClientSetting.class, RestClientConfig.class, RestClientConfigForRestClientTest.ContextConfiguration.class })
public class RestClientConfigForRestClientTest {

    @Autowired
    private RestClientSetting setting;

    @Autowired
    private RestTemplate objectUnderTest;

    @Test
    public void shouldLoadTestProperties() {
        // given & when & then
        assertThat(setting).isNotNull();
        assertThat(setting.getUser()).isNull();
    }

    @Test
    public void shouldActivateRestAuthClient() {
        Optional<ClientHttpRequestInterceptor> basicAuthInterceptor = objectUnderTest.getInterceptors()
                .stream()
                .filter(inter -> inter instanceof BasicAuthorizationInterceptor)
                .findFirst();
        assertThat(basicAuthInterceptor.isPresent()).isFalse();
    }

    @TestConfiguration
    @EnableConfigurationProperties
    static class ContextConfiguration {

    }
}
