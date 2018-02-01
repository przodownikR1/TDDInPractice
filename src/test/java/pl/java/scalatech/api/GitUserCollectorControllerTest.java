package pl.java.scalatech.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.java.scalatech.TestTools.javaTimeModule;
import static pl.java.scalatech.TestTools.prepareRepoInfoSample;

import pl.java.scalatech.dto.RepoInfo;
import pl.java.scalatech.service.GitHubService;

@RunWith(SpringRunner.class)
@WebMvcTest(GitUserCollectorController.class)
public class GitUserCollectorControllerTest {

    @MockBean
    private GitHubService gitHubService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<RepoInfo> json;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }
    
  
    @Test
    public void shouldReturnCorrectAnswer() throws Exception {
        // given
        String owner = "przodownikR1";
        String repoName = "basicAuth";
        RepoInfo expectedResult = prepareRepoInfoSample(repoName);
        given(gitHubService.getRepoInfo(owner, repoName))
                .willReturn(Optional.of(expectedResult));
        
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/repositories/{owner}/{repoName}",owner,repoName)
                        .accept(MediaType.APPLICATION_JSON))
                         .andExpect(status().isOk())
                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                         .andReturn().getResponse();

        // then
       assertThat(response.getStatus()).isEqualTo(OK.value());
       assertThat(response.getContentAsString()).isEqualTo(json.write(expectedResult).getJson());
       verify(gitHubService).getRepoInfo(owner, repoName);
    }


}