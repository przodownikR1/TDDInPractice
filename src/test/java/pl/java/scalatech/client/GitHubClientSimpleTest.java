package pl.java.scalatech.client;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import pl.java.scalatech.TestTools;
import pl.java.scalatech.dto.RepoInfo;
@RunWith(MockitoJUnitRunner.class)
public class GitHubClientSimpleTest {
    
    private GitHubRestClient objectUnderTest;
    
    @Mock
    private RestTemplate restTemplate;
    
    @Before
    public void setup() {
        objectUnderTest = new GitHubRestClient(restTemplate);
    }
    
    @Test
    public void shouldReturnCorrectlyAnswer() throws MalformedURLException {
        //given
        String repoName = "basicAuth";
        String owner = "przodownikR1";
        RepoInfo expectedAnswer = TestTools.prepareRepoInfoSample(repoName);
        String url = "https://api.github.com/repos/{owner}/{repo_name}";
        ArgumentCaptor<Map<String,Object>> paramsArg = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        Map<String, Object> parameters = ImmutableMap.of("owner", owner, "repo_name", repoName);
        ResponseEntity<RepoInfo> expectedClientAnswer = ResponseEntity.ok(expectedAnswer);
        BDDMockito.given(restTemplate.getForEntity(Mockito.eq(url),Mockito.eq(RepoInfo.class),Mockito.eq(parameters))).willReturn(expectedClientAnswer);
        //when
        Optional<RepoInfo> clientResult = objectUnderTest.findRepoByName("przodownikR1", repoName);
        //then
        assertThat(clientResult.isPresent()).isTrue();
        assertThat(clientResult.get()).isEqualTo(expectedAnswer);
        verify(restTemplate).getForEntity(urlArg.capture(),Mockito.eq(RepoInfo.class),paramsArg.capture());
        assertThat(urlArg.getValue()).isEqualTo(url); 
        assertThat(paramsArg.getValue()).isEqualTo(parameters);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void shouldGenerateEmptyResultIfSearchResultFails () throws MalformedURLException {
        //given
        String repoName = "basicAuth";
        String owner = "przodownikR1";
        String url = "https://api.github.com/repos/{owner}/{repo_name}";
        Map<String, Object> parameters = ImmutableMap.of("owner", owner, "repo_name", repoName);
        ResponseEntity<RepoInfo> expectedClientAnswer = ResponseEntity.notFound().build();
        BDDMockito.given(restTemplate.getForEntity(Mockito.eq(url),Mockito.eq(RepoInfo.class),Mockito.eq(parameters))).willReturn(expectedClientAnswer);
        ArgumentCaptor<Map<String,Object>> paramsArg = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        //when
        Optional<RepoInfo> clientResult = objectUnderTest.findRepoByName("przodownikR1", repoName);
        //then
        assertThat(clientResult.isPresent()).isFalse();
        assertThat(clientResult).isEqualTo(Optional.empty());
        verify(restTemplate).getForEntity(urlArg.capture(),Mockito.eq(RepoInfo.class),paramsArg.capture());
        assertThat(urlArg.getValue()).isEqualTo(url); 
        assertThat(paramsArg.getValue()).isEqualTo(parameters);
        verifyNoMoreInteractions(restTemplate);
        
    }
}
