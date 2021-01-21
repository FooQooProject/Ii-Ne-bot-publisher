package com.fooqoo56.iine.bot.publisher.infrastructure.api.config;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.interceptor.Oauth2Interceptor;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.interceptor.RestRequestInterceptor;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@ConstructorBinding
@ConfigurationProperties(prefix = "extension.api.twitter")
@RequiredArgsConstructor
@Getter
public class TwitterConfig {

    private final String baseUrl;

    private final String path;

    private final Duration connectTimeout;

    private final Duration readTimeout;

    private final String apikey;

    private final String apiSecret;

    private final String accessToken;

    private final String accessTokenSecret;

    /**
     * Twitter APIのTemplate.
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate twitterSearchTemplate(final Oauth2Interceptor twitterOauth2Interceptor) {
        return RestTemplateConfig.restTemplateBuilder()
                .additionalInterceptors(twitterOauth2Interceptor, new RestRequestInterceptor())
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
    }

    /**
     * Twitter Favorite APIのTemplate.
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate twitterFavoriteTemplate() {
        return RestTemplateConfig.restTemplateBuilder()
                .additionalInterceptors(new RestRequestInterceptor())
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
    }
}
