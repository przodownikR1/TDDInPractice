package pl.java.scalatech.config;

import lombok.RequiredArgsConstructor;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
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
    RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
