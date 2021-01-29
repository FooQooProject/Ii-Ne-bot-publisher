package com.fooqoo56.iine.bot.publisher.infrastructure.api.config;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.filter.Oauth2Filter;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.filter.RestRequestFilter;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.interceptor.Oauth2Interceptor;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.interceptor.RestRequestInterceptor;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

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

    private final Integer maxInMemorySize;

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

    @Bean
    public WebClient twitterSearchClient(final Oauth2Filter oauth2Filter,
                                         final RestRequestFilter restRequestFilter) {
        final HttpClient httpClient = HttpClient.create()
                .responseTimeout(readTimeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart());

        final ReactorClientHttpConnector connector =
                new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(maxInMemorySize))
                        .build())
                .clientConnector(connector)
                .filter(oauth2Filter)
                .filter(restRequestFilter)
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

    @Bean
    public WebClient twitterFavoriteClient(final RestRequestFilter restRequestFilter) {
        final HttpClient httpClient = HttpClient.create()
                .responseTimeout(readTimeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart());

        final ReactorClientHttpConnector connector =
                new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
                .filter(restRequestFilter)
                .build();
    }
}
