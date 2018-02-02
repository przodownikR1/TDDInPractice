package pl.java.scalatech.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import pl.java.scalatech.TestTools;
import pl.java.scalatech.dto.RepoInfo;

@RunWith(SpringRunner.class)
@RestClientTest(GitHubRestClient.class)
@Ignore
public class GitHubClientTest {
    @Autowired
    private GitHubRestClient objectUnderTest;

    @Autowired
    private MockRestServiceServer server;
    private static MockServerRestTemplateCustomizer customizer; 
    @Autowired
    private ObjectMapper objectMapper;

    private RepoInfo expectedAnswer;

    @Before
    public void setUp() throws Exception {
        expectedAnswer = TestTools.prepareRepoInfoSample("-vava_11");
        String repoInfoString = objectMapper.writeValueAsString(expectedAnswer);
        this.server.expect(requestTo("/repository/przodownikR1/-vava_11")).andRespond(withSuccess(repoInfoString, APPLICATION_JSON));
    }

    @Test
    public void shouldReturnCorrectlyAnswer() {
        // given
        String owner = "przodownikR1";
        String repoName = "-vava_11";
        // when
        Optional<RepoInfo> info = this.objectUnderTest.findRepoByName(owner, repoName);
        // then
        assertThat(info.isPresent()).isTrue();
        assertThat(info.get()).isEqualTo(expectedAnswer);
        server.verify();

    }
   
    @TestConfiguration
    static class Config {
        
        @Bean
        @Primary
        public RestTemplateBuilder provideBuilder() {
          customizer = new MockServerRestTemplateCustomizer();
          return new RestTemplateBuilder(customizer);
        }
        @Bean
        @Primary
        RestTemplate restTemplate() {
            return provideBuilder().build();
        }
        
        @Bean
        @Primary
        GitHubRestClient gitHubRestClient(RestTemplateBuilder builder) {
            return new GitHubRestClient(builder.build());
        }

    }
}