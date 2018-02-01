package pl.java.scalatech;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GitUserInfoCollectorApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void shouldBootstrapContext() {
        assertThat(true);
    }
    
    @Test
    public void shouldHealthOk() {
        ResponseEntity<String> resultHealthStatus = restTemplate.getForEntity("/actuator/health", String.class);
        Assertions.assertThat(resultHealthStatus.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(resultHealthStatus.getBody()).
    }

}
