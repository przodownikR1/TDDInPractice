package pl.java.scalatech.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import pl.java.scalatech.client.GitHubRestClient;
import pl.java.scalatech.service.GitHubService;

@Configuration
@EnableCaching
public class CollectorConfig {

    @Bean
    GitHubService gitHubService(GitHubRestClient gitHubRestClient) {
        return new GitHubService(gitHubRestClient);
    }

    @Bean
    GitHubRestClient gitHubRestClient(RestTemplate restTemplate) {
        return new GitHubRestClient(restTemplate);
    }


}
