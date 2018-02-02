package pl.java.scalatech.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import pl.java.scalatech.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile(Profiles.DEV)

@RequiredArgsConstructor
class SwaggerConfig implements WebMvcConfigurer {

    private static final String WEBJARS = "/webjars/**";
    private static final String SWAGGER_UI_HTML = "swagger-ui.html";
    private static final String PACKAGE = "pl.java.scalatech";

    private static final String DOCUMENTATION_TITLE = "documentation";
    private static final String DOCUMENTATION_DESCRIPTION = "REST API";

    private final Environment env;

    @Bean
    Docket api() {
        String version = "0.1";
        String host = "localhost:" + env.getProperty("server.port");
        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage(PACKAGE))
                .paths(any())
                .build()
                .host(host)
                .enable(true)
                .apiInfo(new ApiInfoBuilder()
                        .title(DOCUMENTATION_TITLE)
                        .description(DOCUMENTATION_DESCRIPTION)
                        .version(version).build())
                .useDefaultResponseMessages(false);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SWAGGER_UI_HTML).addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler(WEBJARS).addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}