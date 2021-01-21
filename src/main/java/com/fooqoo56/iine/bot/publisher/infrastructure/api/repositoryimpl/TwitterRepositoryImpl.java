package com.fooqoo56.iine.bot.publisher.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.publisher.domain.api.repository.TwitterRepository;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.config.TwitterConfig;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.util.OauthAuthorizationHeaderBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@RequiredArgsConstructor
public class TwitterRepositoryImpl implements TwitterRepository {

    private final TwitterConfig config;
    private final RestTemplate twitterSearchTemplate;
    private final RestTemplate twitterFavoriteTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetListResponse findTweet(final TweetRequest request) {
        final HttpHeaders headers = new HttpHeaders();

        final String url =
                UriComponentsBuilder.fromHttpUrl(config.getBaseUrl())
                        .path(getSearchPath())
                        .queryParams(request.getQueryMap())
                        .build()
                        .toString();

        return twitterSearchTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<String>(headers),
                        TweetListResponse.class)
                .getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetResponse favoriteTweet(final String id) {

        final String url =
                UriComponentsBuilder.fromHttpUrl(config.getBaseUrl())
                        .path(getFavoritePath())
                        .queryParam("id", id)
                        .build()
                        .toString();

        return twitterFavoriteTemplate
                .exchange(url, HttpMethod.POST, new HttpEntity<String>(getOauth1Header(id)),
                        TweetResponse.class)
                .getBody();
    }

    /**
     * OAuth1.0aの認証付きヘッダを取得する.
     *
     * @param id ツイートID
     * @return ヘッダ
     */
    private HttpHeaders getOauth1Header(final String id) {
        final HttpHeaders headers = new HttpHeaders();

        final OauthAuthorizationHeaderBuilder builder = OauthAuthorizationHeaderBuilder
                .builder()
                .method(HttpMethod.POST.name().toUpperCase())
                .url(config.getBaseUrl() + getFavoritePath())
                .consumerSecret(config.getApiSecret())
                .tokenSecret(config.getAccessTokenSecret())
                .accessToken(config.getAccessToken())
                .consumerKey(config.getApikey())
                .id(id)
                .build();

        headers.set(HttpHeaders.AUTHORIZATION,
                builder.getOauthHeader()
        );

        return headers;
    }

    /**
     * searchパス取得.
     *
     * @return パス
     */
    private String getSearchPath() {
        return config.getPath() + "search/tweets.json";
    }

    /**
     * いいねパス取得.
     *
     * @return パス
     */
    private String getFavoritePath() {
        return config.getPath() + "favorites/create.json";
    }
}
