package pl.java.scalatech.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static pl.java.scalatech.TestTools.prepareRepoInfoSample;

import pl.java.scalatech.client.GitHubRestClient;
import pl.java.scalatech.dto.RepoInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GitHubServiceContextTest.Configuration.class)
public class GitHubServiceContextTest {

    @MockBean
    private GitHubRestClient restClient;

    @Autowired
    private GitHubService objectUnderTest;

    @Test
    @Ignore
    public void shouldReturnCorrectAnswer() throws MalformedURLException {
        // given
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        RepoInfo expectedAnswer = prepareRepoInfoSample(repoName);
        given(restClient.findRepoByName(owner, repoName)).willReturn(Optional.of(expectedAnswer));
        // when
        Optional<RepoInfo> repoInfo = objectUnderTest.getRepoInfo(owner, repoName);
        // then
        assertThat(repoInfo.isPresent());
        assertThat(repoInfo.get()).isEqualTo(expectedAnswer);
        verify(restClient).findRepoByName(owner, repoName);
        verifyNoMoreInteractions(restClient);
    }

    @Test
    public void shouldReturnWrongAnswer() {
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        given(restClient.findRepoByName(owner, repoName)).willReturn(Optional.empty());
        // when
        Optional<RepoInfo> repoInfo = objectUnderTest.getRepoInfo(owner, repoName);
        // then
        assertThat(repoInfo.isPresent()).isFalse();
        verify(restClient).findRepoByName(owner, repoName);
        verifyNoMoreInteractions(restClient);
    }

    @TestConfiguration
    static class Configuration {

    }
}
