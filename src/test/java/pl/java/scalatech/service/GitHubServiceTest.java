package pl.java.scalatech.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.net.MalformedURLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.java.scalatech.TestTools;
import pl.java.scalatech.client.GitHubRestClient;
import pl.java.scalatech.dto.RepoInfo;

@RunWith(JUnitParamsRunner.class)
public class GitHubServiceTest {
    @Mock
    private GitHubRestClient restClient;

    private GitHubService objectUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        objectUnderTest = new GitHubService(restClient);
    }

    @Test
    public void shouldReturnCorrectAnswer() throws MalformedURLException {
        // given
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        RepoInfo expectedAnswer = TestTools.prepareRepoInfoSample(repoName);
        ArgumentCaptor<String> ownerVar = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> repoVar = ArgumentCaptor.forClass(String.class);
        given(restClient.findRepoByName(owner, repoName)).willReturn(Optional.of(expectedAnswer));
        // when
        Optional<RepoInfo> repoInfo = objectUnderTest.getRepoInfo(owner, repoName);
        // then
        assertThat(repoInfo.isPresent()).isTrue();
        assertThat(repoInfo.get()).isEqualTo(expectedAnswer);
        verify(restClient).findRepoByName(ownerVar.capture(), repoVar.capture());
        assertThat(ownerVar.getValue()).isEqualTo(owner);
        assertThat(repoVar.getValue()).isEqualTo(repoName);
        verifyNoMoreInteractions(restClient);
    }

    @Test
    public void shouldReturnWrongAnswer() {
        // given
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        given(restClient.findRepoByName(owner, repoName)).willReturn(Optional.empty());
        ArgumentCaptor<String> ownerVar = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> repoVar = ArgumentCaptor.forClass(String.class);
        // when
        Optional<RepoInfo> repoInfo = objectUnderTest.getRepoInfo(owner, repoName);
        // then
        assertThat(repoInfo.isPresent()).isFalse();
        verify(restClient).findRepoByName(ownerVar.capture(), repoVar.capture());
        assertThat(ownerVar.getValue()).isEqualTo(owner);
        assertThat(repoVar.getValue()).isEqualTo(repoName);
        verifyNoMoreInteractions(restClient);
    }

    @Parameters({ "przodownik,", ",basicAuth" })
    @Test
    public void shouldThrowExceptionWhenOneOfParamsWasEmpty(String owner, String repoName) {
        // given & when & then
        assertThatThrownBy(() -> objectUnderTest.getRepoInfo(owner, repoName)).isInstanceOf(IllegalArgumentException.class);

    }
    @Test
    public void shouldThrowExceptionWhenOwnerIsNull() {
        assertThatThrownBy(() -> objectUnderTest.getRepoInfo(null, "someRepoName")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void shouldThrowExceptionWhenRepoNameIsNull() {
        assertThatThrownBy(() -> objectUnderTest.getRepoInfo("some user", null)).isInstanceOf(IllegalArgumentException.class);
    }
}
