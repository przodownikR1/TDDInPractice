package pl.java.scalatech.api;

import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.OK;
import static pl.java.scalatech.TestIntegrationTools.prepareRepoInfoSample;

import pl.java.scalatech.dto.RepoInfo;
import pl.java.scalatech.service.GitHubService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EndpointAPITest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private GitHubService gitHubService;

    @Test
    public void shouldEnpointWorkCorrectly() throws MalformedURLException {
        // given
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        RepoInfo expectedResult = prepareRepoInfoSample(repoName);
        Optional<RepoInfo> mockExpectedResult = Optional.of(expectedResult);
        given(gitHubService.getRepoInfo(owner, repoName)).willReturn(mockExpectedResult);
        Map<String, ?> params = ImmutableMap.of("owner", owner, "repo-name", repoName);
        // when
        ResponseEntity<RepoInfo> result = this.restTemplate.getForEntity("/repositories/{owner}/{repo-name}", RepoInfo.class, params);
        // then
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(expectedResult);
        verify(gitHubService).getRepoInfo(owner, repoName);
        verifyNoMoreInteractions(gitHubService);

    }
}
