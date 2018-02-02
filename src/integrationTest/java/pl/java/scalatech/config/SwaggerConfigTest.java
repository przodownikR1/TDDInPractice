package pl.java.scalatech.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerConfigTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldSwaggerUIResponse() throws Exception {
        //given & when
        String body = this.restTemplate.getForObject("/v2/api-docs", String.class);
        //then
        assertThat(body).contains("swagger");

    }
}
