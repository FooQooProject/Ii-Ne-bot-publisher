package com.fooqoo56.iine.bot.publisher.infrastructure.api.config;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.interceptor.RestRequestInterceptor;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@ConstructorBinding
@ConfigurationProperties(prefix = "extension.oauth.twitter")
@RequiredArgsConstructor
@Getter
public class BearerTokenClientConfig {

    private final String baseUrl;
    private final String path;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final String apikey;
    private final String apiSecret;

    /**
     * Bearer APIのTemplate.
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate bearerTokenTwitterTemplate() {
        return RestTemplateConfig.restTemplateBuilder()
                .additionalInterceptors(new RestRequestInterceptor())
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .basicAuthentication(apikey, apiSecret)
                .build();
    }
}
