package pl.java.scalatech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import pl.java.scalatech.client.GitHubRestClient;
import pl.java.scalatech.dto.RepoInfo;

@RequiredArgsConstructor
@Slf4j
public class GitHubService {
    private final GitHubRestClient githubRestClient;

    @Cacheable("repoInfo")
    public Optional<RepoInfo> getRepoInfo(String owner, String repoName) {
        checkPreconditions(owner, repoName);
        log.info("getRepoInfo : repo owner :  {}, repoName : {}", owner, repoName);
        return githubRestClient.findRepoByName(owner, repoName);
    }

    private void checkPreconditions(String owner, String repoName) {
        checkArgument(!isNullOrEmpty(owner));
        checkArgument(!isNullOrEmpty(repoName));
    }

}
