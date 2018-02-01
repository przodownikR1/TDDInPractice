package pl.java.scalatech.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
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
            ;
        } catch (Exception ex) {
            builder.down(ex);
        }
    }
}