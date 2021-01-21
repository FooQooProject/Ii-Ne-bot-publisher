package com.fooqoo56.iine.bot.publisher.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.config.BearerTokenClientConfig;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.Oauth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@RequiredArgsConstructor
public class BearerTokenClient {

    private final BearerTokenClientConfig config;
    private final RestTemplate bearerTokenTwitterTemplate;

    /**
     * トークン取得.
     *
     * @return Oauth2Response
     */
    public Oauth2Response getBearerToken() {

        final String url =
                UriComponentsBuilder.fromHttpUrl(config.getBaseUrl())
                        .path(config.getPath())
                        .build()
                        .toString();

        return bearerTokenTwitterTemplate
                .exchange(url, HttpMethod.POST, new HttpEntity<>(getBody(), new HttpHeaders()),
                        Oauth2Response.class)
                .getBody();
    }

    /**
     * Oauth2の必須パラメータ取得.
     *
     * @return MultiValueMap
     */
    private MultiValueMap<String, String> getBody() {
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        return body;
    }
}
