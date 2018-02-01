package pl.java.scalatech.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "rest-client")
@Data
@Validated
public class RestClientSetting {

    @NotNull
    private int maxConnection;

    @NotNull
    private int maxPerRoute;

    @NotNull
    private int retryTimes;

    @NotNull
    private int connTimeout;

    @NotNull
    private int readTimeout;

    @NotNull
    private int connWaitTimeout;
}
