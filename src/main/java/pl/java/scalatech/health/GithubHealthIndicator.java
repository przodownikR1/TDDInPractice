package pl.java.scalatech.health;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
class GithubHealthIndicator extends AbstractHealthIndicator {

    private final RestTemplate restTemplate;

    public GithubHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity("https://api.github.com", String.class);
            builder.up().withDetail("statusCode", forEntity.getStatusCode());
        } catch (Exception ex) {
            log.error("Service is down at the moment ",ex);
            builder.down(ex);
        }
    }
}