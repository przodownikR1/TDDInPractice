package pl.java.scalatech.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.java.scalatech.Profiles.DEV;

@RunWith(SpringRunner.class)
@ActiveProfiles(DEV)
@WebAppConfiguration
@ContextConfiguration
public class SwaggerConfigTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void shouldcreateSpringfoxSwaggerJson() throws Exception {
        // given & when & then
        mockMvc.perform(get("/v2/api-docs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swagger", is("2.0")))
                .andExpect(jsonPath("$.info.description", is("REST API")))
                .andExpect(jsonPath("$.info.version", is("0.1")))
                .andExpect(jsonPath("$.info.title", is("documentation")))
                .andExpect(jsonPath("$.host", containsString("localhost")))
                .andExpect(jsonPath("$.basePath", is("/")))
                .andReturn();
    }

    @TestConfiguration
    @EnableWebMvc
    @Import(SwaggerConfig.class)
    static class Config {

    }

}