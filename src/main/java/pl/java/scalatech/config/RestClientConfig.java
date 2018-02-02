package pl.java.scalatech.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RestClientConfig {

    private final RestClientSetting restClientSetting;

    @Bean
    Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    FilterRegistrationBean<Filter> shallowEtagHeaderFilterRegistration() {
        FilterRegistrationBean<Filter> result = new FilterRegistrationBean<>();
        result.setFilter(this.shallowEtagHeaderFilter());
        result.addUrlPatterns("/repositories/*");
        result.setName("shallowEtagHeaderFilter");
        result.setOrder(1);
        return result;
    }
    

    @Bean
    HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(restClientSetting.getMaxConnection());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(restClientSetting.getMaxPerRoute());
        return poolingHttpClientConnectionManager;
    }

    @Bean
    HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager());
        HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(restClientSetting.getMaxPerRoute(), true);
        httpClientBuilder.setRetryHandler(httpRequestRetryHandler);
        return httpClientBuilder;
    }

    @Bean
    HttpClient httpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    @Bean
    ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setReadTimeout(restClientSetting.getReadTimeout());
        clientHttpRequestFactory.setConnectTimeout(restClientSetting.getConnTimeout());
        clientHttpRequestFactory.setConnectionRequestTimeout(restClientSetting.getConnTimeout());
        return clientHttpRequestFactory;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "rest-client.user", matchIfMissing = false)
    RestTemplate restTemplateAuthBasic(ClientHttpRequestFactory factory) {
        log.info("[AUTH] RestTemplate with basicAuth enable....");
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(restClientSetting.getUser(), restClientSetting.getPersonalToken()));
        return restTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "rest-client.user", matchIfMissing = true)
    RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        log.info("[NO AUTH] restTemplate without auth enable....");
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().clear();
        return restTemplate;
    }
}
