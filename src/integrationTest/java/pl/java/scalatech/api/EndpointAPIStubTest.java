package pl.java.scalatech.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static pl.java.scalatech.TestIntegrationTools.buildUnderTestURL;
import static pl.java.scalatech.TestTools.javaTimeModule;

import pl.java.scalatech.dto.RepoInfo;

@RunWith(SpringRunner.class)
public class EndpointAPIStubTest {
    private static final String TESTED_ENDPOINT_URL = "/repository/przodownikR1/-vava_11";
    private static final int WIRE_MOCK_PORT = 9990;
    private static final String GITHUB_EXAMPLE_JSON_FILE = "./src/integrationTest/resources/gitHubExampleAnswer.json";
    private static final String gitHubFilecontent = contentOf(new File(GITHUB_EXAMPLE_JSON_FILE), Charset.forName("UTF-8"));
    private static final String EXPECTED_JSON_FILE = "./src/integrationTest/resources/repoInfo.json";
    private static final String expectedJson = contentOf(new File(EXPECTED_JSON_FILE), Charset.forName("UTF-8"));
    private TestRestTemplate restTemplate;
    private JacksonTester<RepoInfo> json;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WIRE_MOCK_PORT);

    @Before
    public void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule());
        JacksonTester.initFields(this, objectMapper);

        restTemplate = new TestRestTemplate();
        stubFor(get(urlEqualTo(TESTED_ENDPOINT_URL))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(gitHubFilecontent.getBytes())));
    }

    @Test
    public void shouldReturnCorrectlyResult() throws IOException {
        // given
        String owner = "przodownikR1";
        String repoName = "-vava_11";
        RepoInfo expectedBody = json.parse(expectedJson.getBytes()).getObject();
        URI underTestURL = buildUnderTestURL(owner, repoName, WIRE_MOCK_PORT);
        // when
        ResponseEntity<RepoInfo> response = restTemplate.getForEntity(underTestURL, RepoInfo.class);
        // then
        assertTrue("Verify Status Code", response.getStatusCode().equals(OK));
        assertThat(response.getBody()).isEqualTo(expectedBody);
        verify(getRequestedFor(urlMatching(TESTED_ENDPOINT_URL)));

    }

}