package pl.java.scalatech.health;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.boot.actuate.health.Status.UP;

public class GithubHealthIndicatorTest {

    private GithubHealthIndicator objectUnderTest;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Health.Builder builder;

    @Before
    public void init() {
        initMocks(this);
        objectUnderTest = new GithubHealthIndicator(restTemplate);
    }

    @Test
    public void shouldReturnOkWhenConnectionWithGithubIsAvailable() throws Exception {
        // given
        ResponseEntity<String> expectedResult = ResponseEntity.ok("up");
        Health.Builder builderR = Health.status(UP).up();
        when(builder.withDetail("statusCode", expectedResult.getStatusCode())).thenReturn(builderR);
        given(restTemplate.getForEntity(Mockito.any(String.class), Mockito.any())).willReturn(ResponseEntity.ok("up"));
        // when
        objectUnderTest.doHealthCheck(builder);
        // then
        verify(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        verify(builder).up();
        verify(builder, times(0)).down();
    }

    @Test
    public void shouldReturnDownWhenConnectionWithGithubIsNotAvailable() throws Exception {
        // given
        RestClientException expectedRestClientException = new RestClientException("connection broken");
        given(restTemplate.getForEntity(Mockito.any(String.class), Mockito.any())).willThrow(expectedRestClientException);
        // when
        objectUnderTest.doHealthCheck(builder);
        // then
        verify(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        verify(builder).down(expectedRestClientException);
        verify(builder, times(0)).up();
    }
}
