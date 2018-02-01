package pl.java.scalatech.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;
import static pl.java.scalatech.TestTools.prepareRepoInfoSample;

@JsonTest
@RunWith(SpringRunner.class)
public class RepoInfoJsonSerializerTest {
    private static final String FILE_JSON = "./src/test/resources/repoInfo.json";
    private static final String content = contentOf(new File(FILE_JSON), Charset.forName("UTF-8"));

    @Autowired
    private JacksonTester<RepoInfo> json;
    private RepoInfo repoInfo;

    @Before
    public void setup() throws MalformedURLException {
        String repoName = "basicAuth";
        repoInfo = prepareRepoInfoSample(repoName);
    }

    @Test
    public void shouldSerializeJson() throws IOException {
        // given & when & then
        assertThat(json.write(repoInfo)).isEqualToJson(content.getBytes());
    }

    @Test
    public void shouldDeserializeJson() throws IOException {
        // given & when & then
        assertThat(json.parse(content)).isEqualTo(repoInfo);
    }

}
