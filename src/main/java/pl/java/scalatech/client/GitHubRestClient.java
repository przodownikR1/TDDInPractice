package pl.java.scalatech.client;

import com.google.common.collect.ImmutableMap;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import pl.java.scalatech.dto.RepoInfo;

@RequiredArgsConstructor
public class GitHubRestClient {
    private static final String REPO_NAME = "repo_name";
    private static final String OWNER = "owner";
    private final RestTemplate restTemplate;

    public Optional<RepoInfo> findRepoByName(String owner, String repoName) {
        Map<String, Object> parameters = ImmutableMap.of(OWNER, owner, REPO_NAME, repoName);
        ResponseEntity<RepoInfo> response = restTemplate.getForEntity("https://api.github.com/repos/{owner}/{repo_name}", RepoInfo.class, parameters);
        if (response.getStatusCode().is2xxSuccessful()) {
            return of(response.getBody());
        }
        return empty();
    }

}
