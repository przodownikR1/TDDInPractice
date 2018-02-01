package pl.java.scalatech.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.dto.RepoInfo;
import pl.java.scalatech.service.GitHubService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repositories")
class GitUserCollectorController {

    private final GitHubService githubService;

    @GetMapping(value = "/{owner}/{repository-name}")
    ResponseEntity<RepoInfo> retrieveUserRepoInfo(@PathVariable("owner") String owner, @PathVariable("repository-name") String repoName) {
        return githubService.getRepoInfo(owner, repoName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
