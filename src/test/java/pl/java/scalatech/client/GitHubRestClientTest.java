package pl.java.scalatech.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.contentOf;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static pl.java.scalatech.TestTools.javaTimeModule;

import pl.java.scalatech.TestTools;
import pl.java.scalatech.dto.RepoInfo;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { GitHubRestClientTest.Config.class })
public class GitHubRestClientTest {
    private static final String FILE_JSON = "./src/test/resources/repoInfo.json";
    private static final String content = contentOf(new File(FILE_JSON), Charset.forName("UTF-8"));
    private int mockPort = 9998;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(mockPort);
   
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objMapper;
    
    @Test
    public void shouldGitHubRestClientCorrectlyAnswer() throws JsonProcessingException, MalformedURLException {
        //given
        String url = "https://api.github.com/repos/{owner}/{repo_name}";
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule());
        byte[] expectedResponseByte = objectMapper.writeValueAsBytes(TestTools.prepareRepoInfoSample(repoName));
        Map<String, ?> params = ImmutableMap.of("owner", owner, "repo-name", repoName);
        
        stubFor(get(urlEqualTo("http://localhost:9998/repos/przodownikR1/basicAuth"))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(expectedResponseByte)));
        // when
        ResponseEntity<RepoInfo> result = this.restTemplate.getForEntity("/repositories/{owner}/{repo-name}", RepoInfo.class, params);
        System.err.println(result);
        

    }
    @Configuration
    static class Config {

        @Bean
        RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
}
}
