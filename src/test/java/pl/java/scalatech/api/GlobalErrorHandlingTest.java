package pl.java.scalatech.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import pl.java.scalatech.errors_handle.handler.GlobalErrorHandling;

public class GlobalErrorHandlingTest {
    private static final String EXPECTED_ERROR_MESSAGE = "unexpected error occured";

    private MockMvc mockMvc;

    @Mock
    private GitUserCollectorController underTestObject;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, new ObjectMapper());
        given(messageSource.getMessage(Mockito.eq("error.unexpectedException"), Mockito.eq(null), Mockito.any(Locale.class)))
                .willReturn(EXPECTED_ERROR_MESSAGE);
        mockMvc = MockMvcBuilders.standaloneSetup(underTestObject).setControllerAdvice(new GlobalErrorHandling(messageSource)).build();
    }

    @Test
    public void shouldGenerate404IfEndpointWasDamage() throws Exception {
        // given
        String owner = "przodownikR1";
        String repoName = "-vava_11";
        given(underTestObject.retrieveUserRepoInfo(Mockito.any(String.class), Mockito.any(String.class))).willThrow(IllegalArgumentException.class);
        // when & then

        MockHttpServletResponse response = mockMvc.perform(
                get("/repositories/{owner}/{repoName}", owner, repoName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(EXPECTED_ERROR_MESSAGE);

    }
}
