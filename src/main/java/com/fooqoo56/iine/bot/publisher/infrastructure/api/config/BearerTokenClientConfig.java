package com.fooqoo56.iine.bot.publisher.infrastructure.api.config;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.filter.RestRequestFilter;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

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
    public WebClient bearerTokenTwitterTemplate(final RestRequestFilter restRequestFilter) {
        final HttpClient httpClient = HttpClient.create()
                .responseTimeout(readTimeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart());

        final ReactorClientHttpConnector connector =
                new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(apikey, apiSecret))
                .filter(restRequestFilter)
                .build();
    }
}
